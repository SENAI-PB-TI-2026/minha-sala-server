ALTER TABLE ocupacoes ADD COLUMN aula_id BIGINT;
ALTER TABLE ocupacoes ADD CONSTRAINT fk_ocupacoes_aula FOREIGN KEY (aula_id) REFERENCES aulas (id);
ALTER TABLE ocupacoes ADD CONSTRAINT uq_ocupacoes_aula UNIQUE (aula_id);
