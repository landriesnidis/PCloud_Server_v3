/*
Navicat MySQL Data Transfer

Source Server         : 本地 - ROOT权限
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : pcloud_v3

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-09-26 20:04:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `md5` varchar(255) NOT NULL,
  `size` int(11) unsigned zerofill DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  `tempcode` varchar(255) NOT NULL,
  PRIMARY KEY (`md5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file
-- ----------------------------
INSERT INTO `file` VALUES ('123', '00000000005', './file.exe', '000');
INSERT INTO `file` VALUES ('456', '00000000020', './test.txt', '001');

-- ----------------------------
-- Table structure for folder
-- ----------------------------
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `foldername` varchar(255) NOT NULL,
  `parentid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of folder
-- ----------------------------
INSERT INTO `folder` VALUES ('1', '1', '我的文件夹', null);
INSERT INTO `folder` VALUES ('2', '1', '子文件夹', '1');
INSERT INTO `folder` VALUES ('3', '3', 'root', null);
INSERT INTO `folder` VALUES ('4', '4', 'root', null);
INSERT INTO `folder` VALUES ('5', '5', 'root', null);
INSERT INTO `folder` VALUES ('6', '6', 'root', null);
INSERT INTO `folder` VALUES ('7', '7', 'root', null);
INSERT INTO `folder` VALUES ('8', '8', 'root', null);
INSERT INTO `folder` VALUES ('9', '8', '电源', '8');
INSERT INTO `folder` VALUES ('10', '8', '电影', '8');
INSERT INTO `folder` VALUES ('11', '8', '电子书', '8');
INSERT INTO `folder` VALUES ('12', '8', '资料', '8');
INSERT INTO `folder` VALUES ('13', '1', '考研学习资料', '2');
INSERT INTO `folder` VALUES ('14', '1', '成龙电影', '2');
INSERT INTO `folder` VALUES ('15', '9', 'root', null);
INSERT INTO `folder` VALUES ('16', '10', 'root', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `usertoken` varchar(255) DEFAULT NULL,
  `logintime` datetime DEFAULT NULL,
  `rootfolder` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '123456', 'c6927399-9d21-11e7-af77-00ad5034f360', '2017-09-19 18:03:52', '1');
INSERT INTO `user` VALUES ('3', 'lgj', '123456', null, null, '3');
INSERT INTO `user` VALUES ('4', 'lwy', '123456', null, null, '4');
INSERT INTO `user` VALUES ('5', 'lgq', '123456', 'a43a69bc-8885-11e7-87a0-00ad5034ee33', '2017-08-24 12:35:22', '5');
INSERT INTO `user` VALUES ('6', 'lgm', '123456', 'a7060e8a-8885-11e7-87a0-00ad5034ee33', '2017-08-24 12:35:27', '6');
INSERT INTO `user` VALUES ('7', 'lgb', '123456', 'a9901b72-8885-11e7-87a0-00ad5034ee33', '2017-08-24 12:35:31', '7');
INSERT INTO `user` VALUES ('8', 'lx', '123456', '01aaa4b1-8888-11e7-87a0-00ad5034ee33', '2017-08-24 12:55:16', '8');
INSERT INTO `user` VALUES ('9', '李贵杰', '123456', '38b0eb8f-9d22-11e7-af77-00ad5034f360', '2017-09-19 18:07:04', '15');
INSERT INTO `user` VALUES ('10', 'lgj005117', '123456', 'd98250e9-9c65-11e7-b724-00ad50343807', '2017-09-18 19:38:11', '16');

-- ----------------------------
-- Table structure for user_file
-- ----------------------------
DROP TABLE IF EXISTS `user_file`;
CREATE TABLE `user_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `file_md5` varchar(255) NOT NULL,
  `folder_id` int(11) NOT NULL,
  `filename` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_file
-- ----------------------------
INSERT INTO `user_file` VALUES ('1', '1', '123', '1', '腾讯QQ.exe');
INSERT INTO `user_file` VALUES ('3', '1', '456', '14', '老干妈炒面做法.MP4');
INSERT INTO `user_file` VALUES ('4', '1', '123', '2', 'CET4成绩单.xlsx');
INSERT INTO `user_file` VALUES ('5', '1', '123', '2', '般若经.pdf');
INSERT INTO `user_file` VALUES ('6', '1', '456', '13', '老干妈炒面做法.MP4');
INSERT INTO `user_file` VALUES ('7', '1', '456', '1', '吉祥三宝.MP3');

-- ----------------------------
-- Procedure structure for CheckFolderPermissions
-- ----------------------------
DROP PROCEDURE IF EXISTS `CheckFolderPermissions`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckFolderPermissions`(IN `userid` int,IN `folderid` int)
BEGIN
	#校验用户的文件夹权限
	IF (SELECT COUNT(*) FROM folder WHERE folder.id=folderid AND folder.user_id=userid)=0 THEN
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "UNKNOW_FOLDER"; 
	END IF;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for FileUpload
-- ----------------------------
DROP PROCEDURE IF EXISTS `FileUpload`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `FileUpload`(IN `token` varchar(255),IN `filemd5` varchar(255),IN `filesize` int(11))
BEGIN
	#定义变量
	DECLARE userid INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#记录文件信息
	INSERT INTO file(file.md5,file.path,file.size,file.tempcode)VALUES(filemd5,filepath,filesize,UUID());

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for GetFileListByFolderId
-- ----------------------------
DROP PROCEDURE IF EXISTS `GetFileListByFolderId`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetFileListByFolderId`(IN `token` varchar(255),IN `folderid` int(11))
BEGIN	
	#定义变量
	DECLARE userid INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	IF folderid<>0  THEN
		#校验用户的文件夹权限
		CALL CheckFolderPermissions(userid,folderid);
	ELSE
		SELECT rootfolder FROM `user` WHERE id=userid INTO folderid;
	END IF;

	#获取文件夹内所有文件的基本信息
	SELECT file.size,file.tempcode,file.md5,user_file.filename,user_file.id from file,user_file 
				 WHERE user_file.user_id=userid AND user_file.folder_id=folderid AND user_file.file_md5=file.md5;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for GetFolderListByFolderId
-- ----------------------------
DROP PROCEDURE IF EXISTS `GetFolderListByFolderId`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetFolderListByFolderId`(IN `token` varchar(255),IN `folderid` int(11))
BEGIN
	#定义变量
	DECLARE userid INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	IF folderid<>0  THEN
		#校验用户的文件夹权限
		CALL CheckFolderPermissions(userid,folderid);
	ELSE
		SELECT rootfolder FROM `user` WHERE id=userid INTO folderid;
	END IF;

	#获取文件夹内所有文件夹的基本信息
	SELECT folder.id,folder.foldername from folder 
				 WHERE folder.user_id=userid AND folder.parentid=folderid;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for UserAddFile
-- ----------------------------
DROP PROCEDURE IF EXISTS `UserAddFile`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UserAddFile`(IN `token` varchar(255),IN `filemd5` varchar(255),IN `newfilename` varchar(255),IN `folderid` int(11))
BEGIN
	#定义变量
	DECLARE userid INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#校验用户的文件夹权限
	CALL CheckFolderPermissions(userid,folderid);
	
	#检查文件对应的MD5是否有记录
	IF CheckFileExists(filemd5)=0 THEN
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "HAVE_NOT_UPLOADED"; 
	END IF;

	#查看是否有同名文件在文件夹中(之前已校验过权限)
	IF (SELECT COUNT(*) FROM user_file WHERE folder_id=folderid AND filename=newfilename LIMIT 1)>0 THEN
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "FILE_ALREADY_EXISTS";
	END IF;

	#记录文件信息
	INSERT INTO user_file(user_file.file_md5,user_file.user_id,user_file.folder_id,user_file.filename)VALUES(filemd5,userid,folderid,newfilename);
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for CheckFileExists
-- ----------------------------
DROP FUNCTION IF EXISTS `CheckFileExists`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `CheckFileExists`(`filemd5` varchar(255)) RETURNS int(2)
BEGIN
	#定义变量
	DECLARE fileCount INT;

	#查询数据库内是否已有该文件
	RETURN (SELECT COUNT(*) FROM `file` WHERE file.md5=filemd5 LIMIT 1);
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for CheckToken
-- ----------------------------
DROP FUNCTION IF EXISTS `CheckToken`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `CheckToken`(`token` varchar(255)) RETURNS int(11)
BEGIN
	#定义变量
	DECLARE userid INT;
	DECLARE lastLoginTime DATETIME;
	
	#获取token对应的user.id
	SELECT id,logintime FROM `user` WHERE `user`.usertoken=token INTO userid,lastLoginTime;

	#token失效
	IF ISNULL(userid) THEN 
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "IDENTITY_INVALID"; 
	END IF;

	#token超时(有效期30分钟)
	IF DATE_ADD(lastLoginTime,INTERVAL 30 MINUTE)<NOW() THEN 
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "LOGIN_TIMEOUT"; 
	END IF;
	
	#更新登录时间
	UPDATE `user` SET logintime=NOW() WHERE id=userid;

	#返回用户的id
	RETURN userid;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for GetFilePathById
-- ----------------------------
DROP FUNCTION IF EXISTS `GetFilePathById`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GetFilePathById`(`token` varchar(255),`fileid` varchar(255)) RETURNS varchar(255) CHARSET utf8
BEGIN
	#定义变量
	DECLARE userid INT;
	DECLARE	filemd5 VARCHAR(255);
	DECLARE filepath VARCHAR(255);

	#校验token是否有效
	SET userid = CheckToken(token);

	#通过文件临时码获取文件的MD5
	#根据文件的MD5判断用户是否拥有指定文件
	SELECT file.path FROM user_file,file WHERE user_file.user_id=userid AND file.id=fileid INTO filepath;

	#返回文件路径
	RETURN filepath;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserCopyFile
-- ----------------------------
DROP FUNCTION IF EXISTS `UserCopyFile`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserCopyFile`(`token` varchar(255),`folder1id` int(11),`folder2id` int(11),`fileid` int(11)) RETURNS int(11)
BEGIN
	#定义变量
	DECLARE userid INT;
	DECLARE isExist INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#校验用户的文件夹权限
	CALL CheckFolderPermissions(userid,folder1id);
	CALL CheckFolderPermissions(userid,folder2id);

	#目标文件夹内是否已存在该文件名的文件
	SELECT COUNT(*) FROM user_file WHERE folder_id=folder2id AND filename=(SELECT filename FROM user_file WHERE id=fileid) LIMIT 1 INTO isExist;
	IF isExist>0 THEN
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "FILE_ALREADY_EXISTS";
	END IF;

	#复制文件信息
	insert into user_file(filename,file_md5,folder_id,user_id) select filename,file_md5,folder_id,user_id from user_file where id=fileid ; 
	
	#修改文件所属目录
	UPDATE user_file SET folder_id=folder2id WHERE id=LAST_INSERT_ID();

	#文件修改目录成功返回1，失败返回0
	RETURN ROW_COUNT();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserCreateFolder
-- ----------------------------
DROP FUNCTION IF EXISTS `UserCreateFolder`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserCreateFolder`(`token` varchar(255),`folderid` int(11),`newfoldername` varchar(255)) RETURNS int(11)
BEGIN
	#定义变量
	DECLARE userid INT;
	DECLARE isExist INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#校验用户的文件夹权限
	CALL CheckFolderPermissions(userid,folderid);

	#查询是否有同名文件夹存在同一目录下
	SELECT COUNT(*) FROM folder WHERE parentid=folderid AND foldername=newfoldername INTO isExist;
	IF isExist>0 THEN
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "FOLDER_ALREADY_EXISTS"; 
	END IF;
	
	#创建新文件夹
	INSERT INTO folder(folder.foldername,folder.user_id,folder.parentid)VALUES(newfoldername,userid,folderid);

	#返回新文件夹的id
	RETURN LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserDeleteFile
-- ----------------------------
DROP FUNCTION IF EXISTS `UserDeleteFile`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserDeleteFile`(`token` varchar(255),`fileid` int(11),`folderid` int(11)) RETURNS int(2)
BEGIN
	#定义变量
	DECLARE userid INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#校验用户的文件夹权限
	CALL CheckFolderPermissions(userid,folderid);

	#删除指定文件记录
	DELETE FROM user_file WHERE user_file.id=fileid AND user_file.user_id=userid AND user_file.folder_id=folderid;

	#删除成功返回1，删除失败返回0
	RETURN ROW_COUNT();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserLogin
-- ----------------------------
DROP FUNCTION IF EXISTS `UserLogin`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserLogin`(`account` varchar(50),`secret` varchar(50)) RETURNS varchar(255) CHARSET utf8
BEGIN
	#定义变量
	DECLARE token VARCHAR(255);
	DECLARE userid INT;

	#验证账号密码
	SELECT id FROM `user` WHERE `username`=`account` AND `password`=`secret` INTO userid; 
	
	#登录失败
	IF ISNULL(userid) THEN 
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "AUTHENTICATION_FAILED"; 
	END IF;
	
	#生成UUID作为token
	SET token = UUID();

	#将新token和登录时间写入表中
	UPDATE `user` SET usertoken=token,logintime=NOW() WHERE id=userid;
	
	RETURN token;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserLogout
-- ----------------------------
DROP FUNCTION IF EXISTS `UserLogout`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserLogout`(`token` varchar(255)) RETURNS int(2)
BEGIN
	#定义变量
	DECLARE userid INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#重置用户的token
	UPDATE `user` SET usertoken=UUID() WHERE id=userid;

	#注销成功返回1，失败返回0
	RETURN ROW_COUNT();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserMoveFile
-- ----------------------------
DROP FUNCTION IF EXISTS `UserMoveFile`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserMoveFile`(`token` varchar(255),`folder1id` int(11),`folder2id` int(11),`fileid` int(11)) RETURNS int(2)
BEGIN
	#定义变量
	DECLARE userid INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#校验用户的文件夹权限
	CALL CheckFolderPermissions(userid,folder1id);
	CALL CheckFolderPermissions(userid,folder2id);

	#修改文件所属目录
	UPDATE user_file SET folder_id=folder2id WHERE id=fileid AND folder_id=folder1id;

	#文件修改目录成功返回1，失败返回0
	RETURN ROW_COUNT();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserRegister
-- ----------------------------
DROP FUNCTION IF EXISTS `UserRegister`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserRegister`(`account` varchar(50),`secret` varchar(50)) RETURNS varchar(255) CHARSET utf8
BEGIN
	#定义变量
	DECLARE isExist INT;
	DECLARE folderid INT;
	DECLARE userid INT;
	DECLARE token VARCHAR(255);
	
	#查询用户名是否存在
	SELECT COUNT(*) FROM `user` WHERE `user`.username=account LIMIT 1 INTO isExist;
	IF isExist>0 THEN
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "ACCOUNT_OCCUPIED"; 
	END IF;

	#生成token
	SET token=UUID();

	#创建用户记录
	INSERT INTO `user`(`user`.username,`user`.`password`,`user`.usertoken,`user`.logintime)VALUES(account,secret,token,NOW());

	#获取用户id
	SET userid=LAST_INSERT_ID();

	#创建用户文件根目录
	INSERT INTO folder(folder.foldername,folder.user_id)VALUES('root',userid);

	#获取根目录的id
	SET folderid=LAST_INSERT_ID();

	#用户与根目录关联
	UPDATE `user` SET `user`.rootfolder=folderid WHERE `user`.id=userid;

	#返回用户的token
	RETURN token;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for UserRenameFile
-- ----------------------------
DROP FUNCTION IF EXISTS `UserRenameFile`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UserRenameFile`(`token` varchar(255),`folderid` int(11),`fileid` int(11),`newfilename` varchar(255)) RETURNS int(2)
BEGIN
	#定义变量
	DECLARE userid INT;
	DECLARE renamefileid INT;
	DECLARE isExist INT;

	#校验token是否有效
	SET userid = CheckToken(token);

	#校验用户的文件夹权限
	CALL CheckFolderPermissions(userid,folderid);

	#新文件名是否已存在
	SELECT COUNT(*),id FROM user_file WHERE folder_id=folderid AND filename=newfilename LIMIT 1 INTO isExist,renamefileid;
	IF isExist>0 THEN
		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = "FILE_ALREADY_EXISTS";
		#RETURN 0;
	END IF;

	#文件重命名
	UPDATE user_file SET filename=newfilename WHERE folder_id=folderid AND id=fileid;

	#重命名成功返回1，失败返回0
	RETURN ROW_COUNT();
END
;;
DELIMITER ;
