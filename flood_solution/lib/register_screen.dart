import 'package:flutter/material.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'login_screen_1.dart';

String email_shared_pref = "test";
String msg_txt = "";
String msg_token = "";

final GlobalKey<FormState> formKey = GlobalKey<FormState>();
List<Item> items = List();
Item item;
DatabaseReference itemRef;

class MyAppReg extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Login Screen 1',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new MyHomePageReg(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class MyHomePageReg extends StatefulWidget {
  @override
  _MyHomePageRegState createState() => new _MyHomePageRegState();
}

class _MyHomePageRegState extends State<MyHomePageReg> {
  //List<Item> items = List();
  //Item item;
  //DatabaseReference itemRef;
  String textValue = "Hello World";
  FirebaseMessaging firebaseMessaging = FirebaseMessaging();

  //final GlobalKey<FormState> formKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();
    getMailPref().then((val) {
      email_shared_pref = val;
      print("Shared pref = $email_shared_pref");
      //print(checkEmailAvailability());
    });

    item = Item("", "", "", "", "", "");
    final FirebaseDatabase database = FirebaseDatabase
        .instance; //Rather then just writing FirebaseDatabase(), get the instance.
    itemRef = database.reference().child('users');
    itemRef.onChildAdded.listen(_onEntryAdded);
    itemRef.onChildChanged.listen(_onEntryChanged);
    itemRef.onChildRemoved.listen(_onEntryRemoved);

    String _message;
    firebaseMessaging.configure(
      onLaunch: (Map<String, dynamic> msg) {
        if (Theme.of(context).platform == TargetPlatform.iOS)
          _message = msg['aps']['alert'];
        else
          _message = msg['notification']['body'];
      },
    );
    firebaseMessaging.requestNotificationPermissions(
        const IosNotificationSettings(sound: true, alert: true, badge: true));
    firebaseMessaging.onIosSettingsRegistered
        .listen((IosNotificationSettings setting) {
      print('IOS setting Registerd');
    });
    firebaseMessaging.getToken().then((token) {
      print("got the token");
      msg_token = token;
      item.fcm_token = token;
    });
  }

  update_fcmToken(String token) {
    print(token);
    DatabaseReference databaseReference = new FirebaseDatabase().reference();
    databaseReference.child('fcm-token/${token}').set({"token": token});
    setState(() {
      textValue = token;
    });
  }

  _onEntryAdded(Event event) {
    setState(() {
      items.add(Item.fromSnapshot(event.snapshot));
      //print(checkEmailAvailability());
    });
  }

  _onEntryChanged(Event event) {
    var old = items.singleWhere((entry) {
      return entry.key == event.snapshot.key;
    });
    setState(() {
      items[items.indexOf(old)] = Item.fromSnapshot(event.snapshot);
    });
    //print(checkEmailAvailability());
  }

  _onEntryRemoved(Event event) {
    var old = items.singleWhere((entry) {
      return entry.key == event.snapshot.key;
    });
    setState(() {
      items.removeAt(items.indexOf(old));
    });
    //print(checkEmailAvailability());
  }

  _onDelete(String key, int index) {
    itemRef.child(key).remove().then((_) {
      print("record deleted");
    });
  }

  /*
  String checkEmailAvailability() {
    if (email_shared_pref == "") {
      setState(() {
        //msg_txt = "Please Register Using Your Name, Emai, Pin ";
        msg_txt = "";
      });
    }
    //Cheking availability of email
    for (var i = 0; i < items.length; i++) {
      if (items[i].mail == email_shared_pref) {
        runApp(MyApp_Msg());
        return items[i].auth;
      }
    }
    setState(() {
      //msg_txt = "Msg : Please Register Using Your Name, Emai, Pin ";
      msg_txt = "";
    });
    return "-1";
  }
  */

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        resizeToAvoidBottomPadding: true,
        backgroundColor: Colors.white,
        body: ListView(
          children: [
            Container(
                child: RegisterScreen(
              primaryColor: Color(0xFF4aa0d5),
              backgroundColor: Colors.white,
              backgroundImage: new AssetImage("assets/images/full-bloom.png"),
            ))
          ],
        ));
  }
}

class RegisterScreen extends StatelessWidget {
  final Color primaryColor;
  final Color backgroundColor;
  final AssetImage backgroundImage;

  RegisterScreen(
      {Key key, this.primaryColor, this.backgroundColor, this.backgroundImage});

  @override
  Widget build(BuildContext context) {
    return new Container(
      //height: MediaQuery.of(context).size.height,
      decoration: BoxDecoration(
        color: this.backgroundColor,
      ),
      child: Form(
        key: formKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.max,
          children: <Widget>[
            new ClipPath(
              clipper: MyClipper(),
              child: Container(
                decoration: BoxDecoration(
                  image: new DecorationImage(
                    image: this.backgroundImage,
                    fit: BoxFit.cover,
                  ),
                ),
                alignment: Alignment.center,
                padding: EdgeInsets.only(top: 150.0, bottom: 100.0),
                child: Column(
                  children: <Widget>[
                    Text(
                      "FLOOD",
                      style: TextStyle(
                          fontSize: 50.0,
                          fontWeight: FontWeight.bold,
                          color: this.primaryColor),
                    ),
                    Text(
                      "SOLUTION",
                      style: TextStyle(
                          fontSize: 20.0,
                          fontWeight: FontWeight.bold,
                          color: this.primaryColor),
                    ),
                  ],
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 40.0),
              child: Text(
                "Email",
                style: TextStyle(color: Colors.grey, fontSize: 16.0),
              ),
            ),
            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color: Colors.grey.withOpacity(0.5),
                  width: 1.0,
                ),
                borderRadius: BorderRadius.circular(20.0),
              ),
              margin: const EdgeInsets.symmetric(
                  vertical: 10.0, horizontal: 20.0),
              child: Row(
                children: <Widget>[
                  new Padding(
                    padding: EdgeInsets.symmetric(
                        vertical: 10.0, horizontal: 15.0),
                    child: Icon(
                      Icons.mail,
                      color: Colors.grey,
                    ),
                  ),
                  Container(
                    height: 30.0,
                    width: 1.0,
                    color: Colors.grey.withOpacity(0.5),
                    margin: const EdgeInsets.only(left: 00.0, right: 10.0),
                  ),
                  new Expanded(
                    child: TextFormField(
                      initialValue: '',
                      onSaved: (val) {
                        item.email = val;
                        email_shared_pref = val;
                        saveMailPref(val);
                      },
                      validator: (val) => val == "" ? val : null,
                      decoration: InputDecoration(
                        border: InputBorder.none,
                        hintText: 'Enter your email',
                        hintStyle: TextStyle(color: Colors.grey),
                      ),
                    ),
                  )
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 40.0),
              child: Text(
                "Name",
                style: TextStyle(color: Colors.grey, fontSize: 16.0),
              ),
            ),
            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color: Colors.grey.withOpacity(0.5),
                  width: 1.0,
                ),
                borderRadius: BorderRadius.circular(20.0),
              ),
              margin: const EdgeInsets.symmetric(
                  vertical: 10.0, horizontal: 20.0),
              child: Row(
                children: <Widget>[
                  new Padding(
                    padding: EdgeInsets.symmetric(
                        vertical: 10.0, horizontal: 15.0),
                    child: Icon(
                      Icons.people,
                      color: Colors.grey,
                    ),
                  ),
                  Container(
                    height: 30.0,
                    width: 1.0,
                    color: Colors.grey.withOpacity(0.5),
                    margin: const EdgeInsets.only(left: 00.0, right: 10.0),
                  ),
                  new Expanded(
                    child: TextFormField(
                      initialValue: '',
                      onSaved: (val) {
                        item.name = val;
                      },
                      validator: (val) => val == "" ? val : null,
                      decoration: InputDecoration(
                        border: InputBorder.none,
                        hintText: 'Enter your name',
                        hintStyle: TextStyle(color: Colors.grey),
                      ),
                    ),
                  )
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 40.0),
              child: Text(
                "Password",
                style: TextStyle(color: Colors.grey, fontSize: 16.0),
              ),
            ),
            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color: Colors.grey.withOpacity(0.5),
                  width: 1.0,
                ),
                borderRadius: BorderRadius.circular(20.0),
              ),
              margin: const EdgeInsets.symmetric(
                  vertical: 10.0, horizontal: 20.0),
              child: Row(
                children: <Widget>[
                  new Padding(
                    padding: EdgeInsets.symmetric(
                        vertical: 10.0, horizontal: 15.0),
                    child: Icon(
                      Icons.lock_open,
                      color: Colors.grey,
                    ),
                  ),
                  Container(
                    height: 30.0,
                    width: 1.0,
                    color: Colors.grey.withOpacity(0.5),
                    margin: const EdgeInsets.only(left: 00.0, right: 10.0),
                  ),
                  new Expanded(
                    child: TextFormField(
                      initialValue: '',
                      onSaved: (val) {
                        item.password = val;
                      },
                      validator: (val) => val == "" ? val : null,
                      obscureText: true,
                      decoration: InputDecoration(
                        border: InputBorder.none,
                        hintText: 'Enter your password',
                        hintStyle: TextStyle(color: Colors.grey),
                      ),
                    ),
                  )
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 40.0),
              child: Text(
                "Confirm Password",
                style: TextStyle(color: Colors.grey, fontSize: 16.0),
              ),
            ),
            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color: Colors.grey.withOpacity(0.5),
                  width: 1.0,
                ),
                borderRadius: BorderRadius.circular(20.0),
              ),
              margin: const EdgeInsets.symmetric(
                  vertical: 10.0, horizontal: 20.0),
              child: Row(
                children: <Widget>[
                  new Padding(
                    padding: EdgeInsets.symmetric(
                        vertical: 10.0, horizontal: 15.0),
                    child: Icon(
                      Icons.lock_open,
                      color: Colors.grey,
                    ),
                  ),
                  Container(
                    height: 30.0,
                    width: 1.0,
                    color: Colors.grey.withOpacity(0.5),
                    margin: const EdgeInsets.only(left: 00.0, right: 10.0),
                  ),
                  new Expanded(
                    child: TextField(
                      obscureText: true,
                      decoration: InputDecoration(
                        border: InputBorder.none,
                        hintText: 'Confirm your password',
                        hintStyle: TextStyle(color: Colors.grey),
                      ),
                    ),
                  )
                ],
              ),
            ),
            Container(
              margin: const EdgeInsets.only(top: 20.0),
              padding: const EdgeInsets.only(left: 20.0, right: 20.0, bottom: 50 ),
              child: new Row(
                children: <Widget>[
                  new Expanded(
                    child: FlatButton(
                      shape: new RoundedRectangleBorder(
                          borderRadius: new BorderRadius.circular(30.0)),
                      splashColor: this.primaryColor,
                      color: this.primaryColor,
                      child: new Row(
                        children: <Widget>[
                          new Padding(
                            padding: const EdgeInsets.only(left: 20.0),
                            child: Text(
                              "REGISTER",
                              style: TextStyle(color: Colors.white),
                            ),
                          ),
                          new Expanded(
                            child: Container(),
                          ),
                          new Transform.translate(
                            offset: Offset(15.0, 0.0),
                            child: new Container(
                              padding: const EdgeInsets.all(5.0),
                              child: FlatButton(
                                shape: new RoundedRectangleBorder(
                                    borderRadius:
                                        new BorderRadius.circular(28.0)),
                                splashColor: Colors.white,
                                color: Colors.white,
                                child: Icon(
                                  Icons.arrow_forward,
                                  color: this.primaryColor,
                                ),
                                onPressed: () => {registerPressed(context)},
                              ),
                            ),
                          )
                        ],
                      ),
                      onPressed: () => {registerPressed(context)},
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class MyClipper extends CustomClipper<Path> {
  @override
  Path getClip(Size size) {
    Path p = new Path();
    p.lineTo(size.width, 0.0);
    p.lineTo(size.width, size.height * 0.85);
    p.arcToPoint(
      Offset(0.0, size.height * 0.85),
      radius: const Radius.elliptical(50.0, 10.0),
      rotation: 0.0,
    );
    p.lineTo(0.0, 0.0);
    p.close();
    return p;
  }

  @override
  bool shouldReclip(CustomClipper oldClipper) {
    return true;
  }
}

void registerPressed(BuildContext context) {
  //function to execute when the login button pressed
  handleSubmit(context);
  print("Register Pressed");
}

class Item {
  String key;
  String email;
  String name;
  String password;
  String lon;
  String lat;
  String fcm_token;

  Item(
      this.email, this.name, this.password, this.lon, this.lat, this.fcm_token);

  Item.fromSnapshot(DataSnapshot snapshot)
      : key = snapshot.key,
        email = snapshot.value["email"],
        name = snapshot.value["name"],
        password = snapshot.value["password"],
        lon = snapshot.value["lon"],
        lat = snapshot.value["lat"],
        fcm_token = snapshot.value["fcm_token"];

  toJson() {
    return {
      "name": name,
      "email": email,
      "password": password,
      "lon": lon,
      "lat": lat,
      "fcm_token": fcm_token,
    };
  }
}

Future<void> saveMailPref(String mail) async {
  SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
  await sharedPreferences.setString("mail", mail);
}

Future<String> getMailPref() async {
  final SharedPreferences sharedPreferences =
      await SharedPreferences.getInstance();
  String mail = await sharedPreferences.getString("mail");
  if (mail == null) {
    return "0";
  } else {
    return mail;
  }
}

void handleSubmit(BuildContext context) {
  final FormState form = formKey.currentState;
  if (form.validate()) {
    form.save();
    form.reset();
  }
  print(item.email);

  //Cheking availability of email
  for (var i = 0; i < items.length; i++) {
    print(items[i].email);
    if (items[i].email == email_shared_pref) {
      print("Mail Found");
      if (items[i].email == item.password) {
        print("all Correct");
        //runApp(MyApp_Msg());
        return;
      } else {
        //Showing the dialog
        showDialog(
          context: context,
          builder: (BuildContext context) {
            // return object of type Dialog
            return AlertDialog(
              title: new Text("Alert"),
              content: new Text(
                  "The email you are using is already in use. Try to Login."),
              actions: <Widget>[
                // usually buttons at the bottom of the dialog
                new FlatButton(
                  child: new Text("ok"),
                  onPressed: () {
                    runApp(MyAppLogin());
                    Navigator.of(context).pop();
                  },
                ),
              ],
            );
          },
        );
        return;
      }
    }
  }
  print("pushed ..................");
  itemRef.push().set(item.toJson());

  showDialog( //dialog to show when registration successfully
    context: context,
    builder: (BuildContext context) {
      // return object of type Dialog
      return AlertDialog(
        title: new Text("Alert"),
        content: new Text(
            "Registration Successful."),
        actions: <Widget>[
          // usually buttons at the bottom of the dialog
          new FlatButton(
            child: new Text("ok"),
            onPressed: () {
              Navigator.of(context).pop();
              runApp(new MyAppLogin());
            },
          ),
        ],
      );
    },
  );
}
