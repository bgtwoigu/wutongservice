
CREATE TABLE IF NOT EXISTS qapk_suggest  (
  sub_id bigint NOT NULL,               -- ��� ר���2000��ʼ��ר����1000��ʼ
  sub_name varchar(255) NOT NULL,  --  ר�⡢ר������
  policy varchar(4096) NOT NULL,             -- ��������
  created_time bigint NOT NULL,         -- ����ʱ��
  destroyed_time bigint NOT NULL,              -- ɾ��ʱ��

  PRIMARY KEY (sub_id),
  INDEX(sub_name),
  INDEX(policy)
) ENGINE InnoDB DEFAULT CHARSET utf8;