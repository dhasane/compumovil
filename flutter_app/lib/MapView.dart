
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutterapp/listaPaises.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter_map/flutter_map.dart';

class mapView extends StatelessWidget {

  final Pais pais;
  mapView({Key key, @required this.pais}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(
              pais.getName.toString()
          ),
        ),
//      body: () => {},
    );
  }

}
