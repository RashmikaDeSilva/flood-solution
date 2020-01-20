import "package:flutter/material.dart";
import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'profilePage.dart';
import 'newsPage.dart';
import 'msgPage.dart';

class MainScreen extends StatefulWidget {
  @override
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  int _page = 0;
  final _pageOption = [
    ProfilePage(),
    NewsPage(),
    MsgPage(),
  ];

  GlobalKey _bottomNavigationKey = GlobalKey();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
          bottomNavigationBar: CurvedNavigationBar(
            key: _bottomNavigationKey,
            index: 0,
            height: 50.0,
            items: <Widget>[
              IconTheme(
                data: new IconThemeData(color: Colors.white),
                child: new Icon(Icons.perm_identity),
              ),
              IconTheme(
                data: new IconThemeData(color: Colors.white),
                child: new Icon(Icons.calendar_today),
              ),
              IconTheme(
                data: new IconThemeData(color: Colors.white),
                child: new Icon(Icons.message),
              ),
            ],
            color: Colors.blueAccent,
            buttonBackgroundColor: Colors.blueAccent,
            backgroundColor: Colors.white,
            animationCurve: Curves.easeInOut,
            animationDuration: Duration(milliseconds: 250),
            onTap: (index) {
              setState(() {
                _page = index;
              });
            },
          ),
          body: Container(child: _pageOption[_page])),
    );
  }
}
