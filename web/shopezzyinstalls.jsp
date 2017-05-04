<!DOCTYPE html>
	<head>
	  <meta charset="utf-8">
	  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	  <title>Mobile Analytics</title>
	  <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	<!-- <script type="text/javascript" src="js/jquery.js"></script> -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>

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

		<body style="font-family: 'Open Sans', sans-serif;">
		
		<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
		
		
		
			  <script type="text/javascript">
			  	$(function () {
    $('#container').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: 'Number of App Installs for ShopEZZY'
        },
        subtitle: {
            text: 'Collected through \'dr\' API'
        },
        xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: { // don't display the dummy year
                day: '%e-%b'
                //month: '%e. %b',
                //year: '%b'
            },
            title: {
                text: 'Date'
            }
        },
        yAxis: {
            title: {
                text: 'Count'
            },
            dateTimeLabelFormats: {
                day: '%e-%b'
            },
            min: 0
        },
        tooltip: {
            headerFormat: '<b>{series.name}</b><br>',
            pointFormat: '{point.x:%e-%b}: {point.y:.2f} nos.'
        },

        plotOptions: {
            /*spline: {
                marker: {
                    enabled: true
                }
            } */

            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },

        series: [{
            name: 'Downloads Starting 4-Apr-2015',
            // Define the data points. All series have a dummy year
            // of 1970/71 in order to be compared on the same x axis. Note
            // that in JavaScript, months start at 0 for January, 1 for February etc.
            data: [ ${total_installs }]
        }]
    });
});
			  </script>
			 	
	</body>
</html>