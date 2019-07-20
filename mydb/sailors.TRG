TYPE=TRIGGERS
triggers='CREATE DEFINER=`skip-grants user`@`skip-grants host` TRIGGER sailorsIns BEFORE INSERT ON Sailors\nFOR EACH ROW BEGIN\nDECLARE msg varchar(255);\nIF NEW.rating < 1 OR NEW.rating > 5 OR NEW.age < 18 THEN\nSET msg = \'Constraints violated!\';\nSIGNAL sqlstate \'45000\' set message_text = msg;\nEND IF;\nEND' 'CREATE DEFINER=`skip-grants user`@`skip-grants host` TRIGGER sailorsUpd BEFORE UPDATE ON Sailors\nFOR EACH ROW BEGIN\nDECLARE msg varchar(255);\nIF NEW.rating < 1 OR NEW.rating > 5 OR NEW.age < 18 THEN\nSET msg = \'Constraints violated!\';\nSIGNAL sqlstate \'45001\' set message_text = msg;\nEND IF;\nEND'
sql_modes=1436549152 1436549152
definers='skip-grants user@skip-grants host' 'skip-grants user@skip-grants host'
client_cs_names='cp850' 'cp850'
connection_cl_names='cp850_general_ci' 'cp850_general_ci'
db_cl_names='latin1_swedish_ci' 'latin1_swedish_ci'
created=156319898109 156319916983
