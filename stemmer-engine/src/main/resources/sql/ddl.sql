CREATE TABLE IF NOT EXISTS word_info (
    id integer not null auto_increment,
	word varchar(30) not null,
	root_word varchar(20) not null,
	prefixes varchar(20),
	suffixes varchar(20),
	primary key(id)
);

CREATE TABLE IF NOT EXISTS kbbi_word_info (
    id integer not null auto_increment,
	word varchar(30) not null,
	word_id integer not null,
	separated_word varchar(40) not null,
	word_type varchar(10) not null,
	root_word varchar(20) not null,
	prefixes varchar(20),
	suffixes varchar(20),
	primary key(id)
);

CREATE TABLE IF NOT EXISTS kbbi_word_means (
	id integer not null auto_increment,
	word varchar(30) not null,
	word_id integer not null,
	means varchar(250),
	usages varchar(250),
	primary key(id)
);

