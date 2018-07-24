drop table if exists links;
create table links (
  id integer primary key autoincrement,
  mini text not null,
  dest text not null
);