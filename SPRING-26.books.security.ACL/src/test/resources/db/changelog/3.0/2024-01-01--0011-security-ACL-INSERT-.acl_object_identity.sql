--changeset solo: 2024-01-01--010-SECURITY-INSERT-acl_object_identity
INSERT INTO public.acl_object_identity (object_id_class,object_id_identity,parent_object,owner_sid,entries_inheriting) VALUES
	 (1,'0',NULL,1,false),
	 (2,'0',NULL,1,false),
	 (3,'0',NULL,1,false),
	 (4,'0',NULL,1,false),
	 (1,'1',NULL,3,false),
	 (1,'2',NULL,3,false),
	 (1,'3',NULL,3,false),
	 (2,'1',NULL,3,false),
	 (2,'2',NULL,3,false),
	 (2,'3',NULL,3,false);
INSERT INTO public.acl_object_identity (object_id_class,object_id_identity,parent_object,owner_sid,entries_inheriting) VALUES
	 (3,'1',NULL,3,false),
	 (3,'2',NULL,3,false),
	 (3,'3',NULL,3,false),
	 (3,'4',NULL,3,false),
	 (3,'5',NULL,3,false),
	 (3,'6',NULL,3,false),
	 (3,'7',NULL,3,false),
	 (3,'8',NULL,3,false),
	 (3,'9',NULL,3,false),
	 (4,'1',NULL,3,false);
INSERT INTO public.acl_object_identity (object_id_class,object_id_identity,parent_object,owner_sid,entries_inheriting) VALUES
	 (4,'2',NULL,3,false),
	 (4,'3',NULL,3,false),
	 (4,'4',NULL,3,false),
	 (4,'5',NULL,3,false),
	 (4,'6',NULL,3,false);
