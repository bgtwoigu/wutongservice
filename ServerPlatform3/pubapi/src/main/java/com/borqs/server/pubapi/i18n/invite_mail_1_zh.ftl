<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 <title>加邀请者为好友</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META content="MSHTML 5.50.4613.1700" name=GENERATOR>
<META name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0">

<link rel="stylesheet"  href="http://${host}/invite_files/jquerymobile/jquery.mobile-1.0rc2.min.css" />
<script src="http://${host}/invite_files/jquerymobile/jquery-1.6.4.min.js"></script>
<script src="http://${host}/invite_files/jquerymobile/jquery.mobile-1.0rc2.min.js"></script>

<script type=text/javascript>
var keyStr = "ABCDEFGHIJKLMNOP" +
"QRSTUVWXYZabcdef" +
"ghijklmnopqrstuv" +
"wxyz0123456789+/" +
"=";

function encode64(input)
{
input = escape(input);
var output = "";
var chr1, chr2, chr3 = "";
var enc1, enc2, enc3, enc4 = "";
var i = 0;

do
{
   chr1 = input.charCodeAt(i++);
   chr2 = input.charCodeAt(i++);
   chr3 = input.charCodeAt(i++);

   enc1 = chr1 >> 2;
   enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
   enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
   enc4 = chr3 & 63;

   if (isNaN(chr2))
   {
    enc3 = enc4 = 64;
   }
   else if (isNaN(chr3))
   {
    enc4 = 64;
   }

   output = output +
   keyStr.charAt(enc1) +
   keyStr.charAt(enc2) +
   keyStr.charAt(enc3) +
   keyStr.charAt(enc4);
   chr1 = chr2 = chr3 = "";
   enc1 = enc2 = enc3 = enc4 = "";
} while (i < input.length);

return output;
}

		function download()
		{
		   document.location = "http://${host}/search?q=com.borqs.qiupu";
		}

		function mutualFriend()
		{
		   var uid = document.form1.uid.value;
		   var fromid = document.form1.fromid.value;

		   document.location = "http://${host}/friend/mutual?user_id=" + uid
		                      + "&from_id=" + fromid;
		}
	</script>
</head>
<body style="overflow-x:hidden; background:#fff; color:#000; font-size:18px;  width:320px; margin:auto;">
<form name="form1" action="" method="post" onsubmit="return false">
<input type="hidden" name="fromid" value="${fromId}" />
<input type="hidden" name="uid" value="${uid}" />
<table cellpadding=1 cellspacing=1 border=0 align=left>
<tr>
<td>
	<table cellpadding=1 cellspacing=1 border=0 align=left name="t1">
	<tr>
	<td align=center valign="bottom">
	<img border=0 src="http://${host}/invite_files/images/logo.jpg"></td>
	</td>
  </tr>
  <tr>
	<td align=left valign=top>尊敬的${name0}：<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${fromName}希望成为您的好友。</td>
	</tr>
	<tr>
	<td align=left valign=top colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
	</table>
</td>
</tr>
<tr>
<td>
	<table cellpadding=1 cellspacing=1 border=0 align=left name="t2">
	<tr>
	<td align=center valign=top>
	<input value="加为好友" ID="button"  type="button" name="addBtn" onclick="mutualFriend()">
  </td>
  <td align=center valign=top>
	<input value="下    载" ID="button"  type="button" name="downBtn" onclick="download()">
	</td>
	</tr>
	<tr>
	<td align=left valign=top colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
	</table>
</td>
</tr>
<tr>
<td>
	<table cellpadding=1 cellspacing=1 border=0 align=left>
	<tr>
	<td align=left valign=top colspan=2>激活播思账号后，您可以免费使用播思账号为您提供的应用和便捷的服务。其中包括秋浦，播思读书，OpenFace等。秋浦是由北京播思软件技术有限公司2011年重磅推出的一款集应用管理、应用商店、社交网络、数据备份等多功能于一体的Android客户端软件。播思读书为您提供分享和阅读图书的丰富体验。OpenFace让您和您的好友面对面无距离沟通。
	</td>
	</tr>
	</table>
</td>
</tr>
<tr>
	<td align=left valign=top colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
<tr>
	<td align=center valign=top colspan=2>© 2007-2012 Borqs 版权所有</td>
</tr>
</table>
</form>
</body>
</html>