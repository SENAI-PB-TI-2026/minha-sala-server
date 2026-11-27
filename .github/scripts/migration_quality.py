"""Diagnostico da convencao e do historico Flyway, sem acessar bancos."""
import collections
import os
from pathlib import Path
import re
import subprocess

ROOT = "src/main/resources/db"
COMMON = f"{ROOT}/migration"
VENDORS = ("h2", "postgresql")
LOCATIONS = (COMMON, *(f"{ROOT}/vendor/{vendor}" for vendor in VENDORS))
# A convencao local e mais restrita que a sintaxe aceita pelo Flyway.
VERSIONED = re.compile(r"V([1-9][0-9]*)\.(0|[1-9][0-9]*)__[a-z0-9]+(?:_[a-z0-9]+)*\.sql")
REPEATABLE = re.compile(r"R__[a-z0-9]+(?:_[a-z0-9]+)*\.sql")
# Reconhece tambem os nomes legados para detectar equivalencias e historico.
FLYWAY_VERSIONED = re.compile(r"V([0-9]+(?:[._][0-9]+)*)__.+\.sql")
FLYWAY_REPEATABLE = re.compile(r"R__.+\.sql")

def git(*args):
    return subprocess.check_output(["git", *args]).decode("utf-8", errors="surrogateescape")

def sql_files(*locations):
    return sorted(p for p in git("ls-files", "-z", "--", *locations).split("\0")
                  if p.lower().endswith(".sql"))

def escape(value, property_value=False):
    value = str(value).replace("%", "%25").replace("\r", "%0D").replace("\n", "%0A")
    if property_value:
        value = value.replace(":", "%3A").replace(",", "%2C")
    return value

def warning(path, message, strict):
    level = "error" if strict else "warning"
    print(f"::{level} file={escape(path, True)}::{escape(message)}")

def version_key(raw):
    parts = [int(part) for part in re.split(r"[._]", raw)]
    while len(parts) > 1 and parts[-1] == 0:
        parts.pop()
    return tuple(parts)

def inspect_sequence(files):
    findings = []
    versions = collections.defaultdict(list)
    sequence = {}
    for path in files:
        name = Path(path).name
        match = VERSIONED.fullmatch(name)
        if match:
            sequence.setdefault(tuple(map(int, match.groups())), path)
        elif not REPEATABLE.fullmatch(name):
            findings.append((path, "Nome fora da convencao V<MAJOR>.<MINOR>__descricao_em_snake_case.sql ou R__descricao_em_snake_case.sql."))
        legacy_match = FLYWAY_VERSIONED.fullmatch(name)
        if legacy_match:
            versions[version_key(legacy_match.group(1))].append(path)
        if Path(path).is_symlink() or not Path(path).is_file():
            findings.append((path, "Migration nao e um arquivo regular."))
    for version, paths in versions.items():
        if len(paths) > 1:
            for path in paths:
                findings.append((path, f"Versao Flyway duplicada: {'.'.join(map(str, version))}."))
    previous = None
    for (major, minor), path in sorted(sequence.items()):
        if previous is None:
            expected = ((1, 0),)
        else:
            prev_major, prev_minor = previous
            expected = ((prev_major, prev_minor + 1), (prev_major + 1, 0))
        if (major, minor) not in expected:
            allowed = " ou ".join(f"V{ma}.{mi}" for ma, mi in expected)
            findings.append((path, f"Sequencia de migrations invalida: esperado {allowed}, encontrado V{major}.{minor}."))
        previous = (major, minor)
    return findings

def inspect_history(base):
    findings = []
    if base:
        if not re.fullmatch(r"[0-9a-f]{40}", base):
            raise ValueError("SHA base invalido")
        diff = git("diff", "--find-renames=100%", "--name-status", "-z", base, "HEAD", "--", *LOCATIONS)
        fields = iter(diff.rstrip("\0").split("\0") if diff else [])
        base_files = git("ls-tree", "-r", "-z", "--name-only", base, "--", *LOCATIONS).split("\0")
        base_versions = [version_key(match.group(1)) for path in base_files
                         if (match := FLYWAY_VERSIONED.fullmatch(Path(path).name))]
        highest_base = max(base_versions, default=None)
        for status in fields:
            path = next(fields)
            if status.startswith("R"):
                destination = next(fields)
                if not path.lower().endswith(".sql") or FLYWAY_REPEATABLE.fullmatch(Path(path).name):
                    continue
                same_script = Path(path).name == Path(destination).name
                h2_relocation = Path(path).parent.as_posix() == COMMON and Path(destination).parent.as_posix() == f"{ROOT}/vendor/h2"
                if same_script and h2_relocation:
                    continue
                findings.append((path, "Migration existente renomeada em relacao a base do PR. Revise o historico antes de aplicar."))
                continue
            if (status.startswith(("M", "D", "T")) and path.lower().endswith(".sql")
                    and not FLYWAY_REPEATABLE.fullmatch(Path(path).name)):
                findings.append((path, "Migration existente modificada, removida ou renomeada em relacao a base do PR. Revise o historico antes de aplicar."))
            match = FLYWAY_VERSIONED.fullmatch(Path(path).name)
            if status == "A" and match and highest_base is not None and version_key(match.group(1)) <= highest_base:
                findings.append((path, "Nova migration com versao anterior ou igual a maior versao na base do PR. Revise a ordem de aplicacao."))
    return findings

if __name__ == "__main__":
    files = sql_files(*LOCATIONS)
    strict = os.getenv("MIGRATION_STRICT", "false").lower() == "true"
    base = os.getenv("BASE_SHA", "")
    findings = inspect_history(base)
    for vendor in VENDORS:
        findings.extend(inspect_sequence(sql_files(COMMON, f"{ROOT}/vendor/{vendor}")))
    findings = list(dict.fromkeys(findings))
    print(f"Arquivos SQL analisados: {len(files)}. Apontamentos: {len(findings)}.")
    if not files:
        print("::warning::Nenhuma migration SQL versionada no diretorio esperado.")
    if not base:
        print("Comparacao de historico indisponivel fora de pull_request.")
    for path, message in findings:
        warning(path, message, strict)
    summary = os.getenv("GITHUB_STEP_SUMMARY")
    if summary:
        with open(summary, "a", encoding="utf-8") as handle:
            handle.write(f"## Integridade das migrations\n\n"
                         f"- Arquivos analisados: {len(files)}\n"
                         f"- Apontamentos: {len(findings)}\n"
                         f"- Modo estrito: {'sim' if strict else 'nao'}\n"
                         f"- Comparacao com a base do PR: {'sim' if base else 'nao'}\n")
    raise SystemExit(1 if strict and findings else 0)
