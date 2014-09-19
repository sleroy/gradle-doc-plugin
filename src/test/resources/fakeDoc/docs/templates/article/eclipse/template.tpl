<!DOCTYPE html>

<html>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>$pagetitle$ - Komea: KPI Dashboard for software
	development</title>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>


<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<link rel="shortcut icon" href="favicon.ico" />
<script type="text/javascript"
	src="https://apis.google.com/js/plusone.js">
        {lang:'en', parsetags:'explicit'}
    </script>

<link href="styles/thymeleaf.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="scripts/shCore.js"></script>
<script type="text/javascript" src="scripts/shBrushXml.js"></script>
<script type="text/javascript" src="scripts/shBrushJava.js"></script>
<link href="/styles/shCore.css" rel="stylesheet" type="text/css" />
<link href="/styles/shThemeThymeleaf.css" rel="stylesheet"
	type="text/css" />

<script type="text/javascript" src="/scripts/thymeleaf-articles.js"></script>

</head>

<body lang="en" dir="ltr">




	<div class="container" role="main">

		<div class="row">
			<div id="bc1" class="btn-group btn-breadcrumb">
				<a href="index.html" class="btn btn-default"><i class="fa fa-home">Komea</i></a>
				<a href="documentation.html" class="btn btn-default"><div>Documentation</div></a>
				<a href="#" class="btn btn-default"><div>$title$</div></a>

			</div>
		</div>

		$if(author)$
		<p id="author">
			<i>By Tocea &mdash; <a href="http://github.com/sleroy/komea"
				target="_blank">https://github.com/sleroy/komea</a></i>
		</p>
		$endif$ $body$


		<div id="footer">
			<div id="googleplus">
				<div id="plusone-div" class="plusone"></div>
				<script type="text/javascript">
            gapi.plusone.render('plusone-div',{"size": "small", "count": "true", "href": "http://www.metrixware.com"});
        </script>
			</div>
			<div id="twitter">
				<a href="http://twitter.com/komea" class="twitter-follow-button"
					data-show-count="false">Follow @komea</a>
				<script src="http://platform.twitter.com/widgets.js"
					type="text/javascript"></script>
			</div>
			<div id="copy">
				Copyright &copy; The <a href="team.html">Tocea Team</a>. See <a
					href="documentation.html">applicable licenses</a>.
			</div>
		</div>


	</div>


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script type="text/javascript">
    SyntaxHighlighter.all();
</script>

</body>

</html>
