<html>
<head>
    <title>Login Page</title>
</head>
<body style="margin:0; padding:0; height:100vh; display:flex; justify-content:center; align-items:center; font-family:Arial; background:#f2f2f2;">

<div style="background:white; padding:30px; border-radius:10px; width:320px; box-shadow:0 0 10px rgba(0,0,0,0.1);">

    <h2 style="text-align:center;">Login Page</h2>

    <pre style="color:red; text-align:center; margin:10px 0;">${errorMessage}</pre>

    <form action="/login" method="post" style="display:flex; flex-direction:column; gap:10px;">

        <label>
            Name:
            <input type="text" name="name" style="width:100%; padding:8px; border:1px solid #ccc; border-radius:5px;">
        </label>

        <label>
            Password:
            <input type="password" name="password"
                   style="width:100%; padding:8px; border:1px solid #ccc; border-radius:5px;">
        </label>

        <input type="submit" value="Submit"
               style="padding:10px; background:#4CAF50; color:white; border:none; border-radius:5px; cursor:pointer;">

    </form>

</div>

</body>
</html>
