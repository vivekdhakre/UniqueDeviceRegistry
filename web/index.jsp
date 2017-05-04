
<html>
	<head>
	  <meta charset="utf-8">
	  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	  <title>Apps URL Generator</title>
	  <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	</head>

	<style type="text/css">
	.main {
		float: left;
		margin-left: 18%;
		margin-top: 10%;
		width: 55%;
	}

	input[type='image'] {
		margin-left: 2%;
		margin-bottom: 5%;
	}
	
	.url {
		font-size: 20px;
		float: left;
		margin-left: 10%;
		width: 75%;
		font-family: 'Open Sans', sans-serif;
	}
	
	.url-list {
		list-style-type:none;
	}
	</style>
	<!-- 
	https://play.google.com/store/apps/details?id=com.ahoy.ureward 
	https://play.google.com/store/apps/details?id=com.ahoy.couponsandhotdeals
	https://play.google.com/store/apps/details?id=com.org.Ahoy
	https://play.google.com/store/apps/details?id=com.ahoy.wififinder
	-->
	<body style="font-family: 'Open Sans', sans-serif;">
		<div class="main">
		 	<input type="image" id="ureward" src="images/ureward.PNG" onclick="myFunction('reward')" />
		 	<input type="image" id="couponsandhotdeals" src="images/chd.PNG" onclick="myFunction('chd')" />
		 	<input type="image" id="Ahoy" src="images/uahoy.PNG" onclick="myFunction('ahoy')" />
		 	<input type="image" id="wififinder" src="images/wifi.PNG" onclick="myFunction('wifi')" />
			<input type="image" id="ngo" src="images/ngo.PNG" onclick="myFunction('ngo')" /><br/>
	 	</div>	

 		<div class="url">
	 	<label>URL&nbsp&nbsp</label>
	 		<input type="text" id="main" 
		 	readonly="readonly" style="width: 80%; font-size: 20px;">
	 	</div>
	 	
	 	<script>
	 	function myFunction(x) {
	 		if(x == 'reward') {
	 	    	document.getElementById('main').value = 'http://uahoy.com/dr/redirect?i=com.ahoy.ureward&aff_sub={aff_sub}';
	 		}
	 		else if(x == 'chd') {
	 	    	document.getElementById('main').value = 'http://uahoy.com/dr/redirect?i=com.ahoy.couponsandhotdeals&aff_sub={aff_sub}';
	 		}
	 		else if(x == 'ahoy') {
	 	    	document.getElementById('main').value = 'http://uahoy.com/dr/redirect?i=com.org.Ahoy&aff_sub={aff_sub}';
	 		}
	 		else if(x == 'wifi') {
	 	    	document.getElementById('main').value = 'http://uahoy.com/dr/redirect?i=com.ahoy.wififinder&aff_sub={aff_sub}';
	 		}
	 		else if(x == 'ngo') {
	 	    	document.getElementById('main').value = 'http://uahoy.com/dr/redirect?i=tushar.ngocommunity&aff_sub={aff_sub}';
	 		}
	 	}
	 	</script>
	</body>
</html>