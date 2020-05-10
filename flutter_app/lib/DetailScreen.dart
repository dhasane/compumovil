
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutterapp/MapView.dart';
import 'package:flutterapp/listaPaises.dart';

class DetailScreen extends StatelessWidget {
  final Pais pais;
  DetailScreen({Key key, @required this.pais}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(pais.getName),
      ),
      body: Container(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Row(
                  children: <Widget>[

                    Container(
                      child: SvgPicture.network(
                          pais.getBandera
                      ),
                      width: 280,
                      height: 80,
                    ),
                    Padding(
                      padding: EdgeInsets.symmetric(vertical: 30, horizontal: 0),
                      child: MaterialButton(
                        onPressed: () => {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => mapView(pais: pais),
                            ),
                          )
                        },
                        minWidth: 50.0,
                        height: 50.0,
                        color: Colors.lightBlueAccent,
                        child: Text(
                          "mapa",
                          style: TextStyle(
                            fontSize: 16.0,
                            color: Colors.white,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
                showInfo( 'Capital' , pais.getCapital),
                showInfo( 'Poblacion' , pais.getPolacion.toString()),
                showInfo( 'Monedas' , pais.getMoneda.toString()),
                showInfo( 'Area', pais.getArea.toString()),
                showInfo( 'Gini' , pais.getGini.toString()),
                showInfo( 'latlng' , pais.getLatlng.toString()),
              ],
            ),
          ),
    );
  }
}

Widget showInfo(String name, String info)
{
  return
    Padding(
      padding: EdgeInsets.all(16.0),
      child: Text( name + ':\n - ' + info),
    );
}

