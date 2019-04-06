-- SQL para Banco de dados PostgreSQL, do sistema Presença UFSC --
drop table aluno;
drop table aula;
drop table instituicao;
drop table disciplina;
drop table cadastro;

-- Tabela Aluno --
create table aluno (
    cod_aluno serial, 
    matricula varchar(255) not null, 
    nome varchar(255) not null, 
    email varchar(255) not null, 
    senha varchar(255) not null, 
    cod_instituicao_cadastrada varchar(255) not null, 
    primary key (matricula)
);

-- Tabela Aula --
create table aula (
    cod_aula serial, 
    data_aula date not null, 
    hora_aula time not null, 
    cod_disciplina varchar(255) not null, 
    cod_aluno varchar(255) not null, 
    localizacao_aluno varchar(255), 
    dentro_perimetro Integer not null, 
    primary key (cod_aula)
);

-- Setando tipo de data;
 SET datestyle = dmy;

-- Tabela Instituicao --
create table instituicao (
    cod_instituicao varchar(255) not null, 
    nome_instituicao varchar(255) not null, 
    latitude_instituicao_min varchar(255) not null, 
    longitude_instituicao_min varchar(255) not null, 
    latitude_instituicao_max varchar(255) not null, 
    longitude_instituicao_max varchar(255) not null, 
    primary key (cod_instituicao)
);

-- Tabela disciplina --
create table disciplina (
  cod_disciplina varchar(255) not null,
  nome_disciplina varchar(255) not null,
  professor_disciplina varchar(255) not null,
  cod_instituicao varchar(255) not null, 
  primary key (cod_disciplina)  
);

-- Tabela de cadastro das disciplinas --
create table cadastro (
    id_cadastro serial,
    cod_disciplina varchar(255) not null,
    cod_aluno varchar(255) not null,
    cod_instituicao varchar(255) not null,
    primary key (id_cadastro)
);


-- Inserts de teste --
insert into instituicao (cod_instituicao, nome_instituicao, latitude_instituicao_min, longitude_instituicao_min, latitude_instituicao_max, longitude_instituicao_max) values 
('UFSC', 'Universidade Federal de Santa Catarina', '-28.952521','-49.469690', '-28.949680', '-49.465510');

insert into disciplina (cod_disciplina, nome_disciplina, professor_disciplina, cod_instituicao) values
('CIT0123', 'Computação distribuída', 'Fulano da Silva', 'UFSC');
