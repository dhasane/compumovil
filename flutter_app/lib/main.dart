import 'package:flutter/material.dart';
import 'package:flutterapp/root_page.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      // home: Home(),
      home: RootPage(),
    );
  }
}

