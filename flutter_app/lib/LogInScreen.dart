
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutterapp/authentication.dart';
import 'package:flutterapp/listaPaises.dart';

class LogInScreen extends StatefulWidget{
  @override
  State<StatefulWidget> createState() => new LogInScreenState();
}

class LogInScreenState extends State<LogInScreen> {
  String _email;
  String _password;
  final BaseAuth auth = Auth();

  final _formKey = GlobalKey<FormState>();

  bool validateAndSave()
  {
    final form = _formKey.currentState;
    if (form.validate()) {
      form.save();
      return true;
    }
    return false;
  }

  void validateUser(Function func){
    print(_email);
    print(_password);
    if( validateAndSave() )
    {
      Future<FirebaseUser> userId = func(_email, _password);
      // Future<FirebaseUser> userId = auth.getCurrentUser();
      userId.then((value) => {
        value != null ? Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => ListaPaises()),
        ) : null
      });
    }
  }

  void validateLogIn() => validateUser(auth.signIn);

  void validateCreateUser() => validateUser(auth.signUp);

  Widget buttons(){
    return Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          MaterialButton(
            onPressed: () => validateLogIn(),
            minWidth: 150.0,
            height: 50.0,
            color: Colors.lightBlueAccent,
            child: Text(
              "LOGIN",
              style: TextStyle(
                fontSize: 16.0,
                color: Colors.white,
              ),
            ),
          ),
          MaterialButton(
            onPressed: () => validateCreateUser(),
            minWidth: 150.0,
            height: 50.0,
            color: Colors.lightBlueAccent,
            child: Text(
              "Create account",
              style: TextStyle(
                fontSize: 16.0,
                color: Colors.white,
              ),
            ),
          ),
        ],
      );
  }

  Form getForm() {
    // Build a Form widget using the _formKey created above.
    return Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Padding(
                padding: EdgeInsets.symmetric(vertical: 5.0, horizontal: 10.0),
                child: TextFormField(
                  keyboardType: TextInputType.emailAddress,
                  decoration: InputDecoration(
                    prefixIcon: Icon(Icons.person),
                    labelText: "Email",
                  ),
                  validator: (value) => value.isEmpty ? 'no puede estar vacio' : null,
                  onSaved: (value) => _email = value.trim(),
                ),
              ),
              Padding(
                padding: EdgeInsets.symmetric(vertical: 5.0, horizontal: 10.0),
                child: TextFormField(
                  obscureText: true,
                  decoration: InputDecoration(
                    prefixIcon: Icon(Icons.lock),
                    labelText: "Password",
                  ),
                  validator: (value) => value.isEmpty ? 'no puede estar vacio' : null,
                  onSaved: (value) => _password = value.trim(),
                ),
              ),
              Padding(
                padding: EdgeInsets.symmetric(vertical: 30.0, horizontal: 10.0),
                child: buttons(),
              )
            ],
          ),
        );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: ListView(
          shrinkWrap: true,
          padding: EdgeInsets.all(15.0),
          children: <Widget>[
            Center(
                child: getForm(),
            ),
          ],
        ),
      ),
    );
  }
}

