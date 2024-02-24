--changeset solo: 2024-02-03--0002-BATCH-SCHEMA-LINKS
CREATE TABLE batch_links (
	id bigserial NOT NULL PRIMARY KEY,
	class_name varchar NULL,
	import_link varchar NULL,
	export_link varchar NULL,
	imported bool DEFAULT false,
	constraint batch_records_pk unique (id)
);
