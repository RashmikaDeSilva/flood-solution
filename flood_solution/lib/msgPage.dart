import "package:flutter/material.dart";
import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_database/ui/firebase_animated_list.dart';

List<Item> items = List();
Item item;
DatabaseReference itemRef;

class MsgPage extends StatefulWidget {
  @override
  _MsgPageState createState() => _MsgPageState();
}

class _MsgPageState extends State<MsgPage> {
  @override
  void initState() {
    super.initState();

    item = Item("", "", "");
    final FirebaseDatabase database = FirebaseDatabase
        .instance; //Rather then just writing FirebaseDatabase(), get the instance.
    itemRef = database.reference().child('users/-LmYPnoZ57UnB5yT-5eB/message');
    itemRef.onChildAdded.listen(_onEntryAdded);
    itemRef.onChildChanged.listen(_onEntryChanged);
    itemRef.onChildRemoved.listen(_onEntryRemoved);
  }

  _onEntryAdded(Event event) {
    setState(() {
      items.add(Item.fromSnapshot(event.snapshot));
      for (int i = 0; i < items.length; i++) {
        print(items[i].body);
        print("-------------------");
      }
    });
  }

  _onEntryChanged(Event event) {
    var old = items.singleWhere((entry) {
      return entry.key == event.snapshot.key;
    });
    setState(() {
      items[items.indexOf(old)] = Item.fromSnapshot(event.snapshot);
    });
    for (int i = 0; i < items.length; i++) {
      print(items[i].body);
      print("-------------------");
    }
  }

  _onEntryRemoved(Event event) {
    var old = items.singleWhere((entry) {
      return entry.key == event.snapshot.key;
    });
    setState(() {
      items.removeAt(items.indexOf(old));
    });
    for (int i = 0; i < items.length; i++) {
      print(items[i].body);
      print("-------------------");
    }
  }

  _onDelete(String key, int index) {
    itemRef.child(key).remove().then((_) {
      print("record deleted");
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: "Hello World",
      home: Scaffold(
        backgroundColor: Colors.white,
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.max,
          children: <Widget>[
            new ClipPath(
              clipper: MyClipper(),
              child: Container(
                decoration: BoxDecoration(
                  image: new DecorationImage(
                    image: new AssetImage("assets/images/full-bloom.png"),
                    fit: BoxFit.cover,
                  ),
                ),
                alignment: Alignment.center,
                padding: EdgeInsets.only(top: 100.0, bottom: 100.0),
                child: Column(
                  children: <Widget>[
                    Text(
                      "RECENT",
                      style: TextStyle(
                          fontSize: 35.0,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF4aa0d5)),
                    ),
                    Text(
                      "MESSAGES",
                      style: TextStyle(
                          fontSize: 20.0,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF4aa0d5)),
                    ),
                  ],
                ),
              ),
            ),
            Flexible(
              child: FirebaseAnimatedList(
                //controller: _controller,
                shrinkWrap: true,
                reverse: true,
                query: itemRef,
                itemBuilder: (BuildContext context, DataSnapshot snapshot,
                    Animation<double> animation, int index) {
                  return new Flex(
                    direction: Axis.vertical,
                    children: [
                      Card(
                        color: items[index].type == "1"
                            ? Colors.blue
                            : items[index].type == "2"
                                ? Colors.green
                                : items[index].type == "3"
                                    ? Colors.brown
                                    : items[index].type == "4"
                                        ? Colors.yellow
                                        : null,
                        elevation: 3.0,
                        margin: EdgeInsets.all(5.0),
                        child: Container(
                          margin: EdgeInsets.all(5.0),
                          child: Flex(
                            direction: Axis.vertical,
                            children: [
                              ListTile(
                                leading: Icon(
                                  Icons.message,
                                  color: Colors.white,
                                ),
                                title: Text(
                                  items[index].title,
                                  style: TextStyle(color: Colors.white),
                                ),
                                subtitle: Text(items[index].body, style: TextStyle(color: Colors.limeAccent),),
                                trailing: Icon(Icons.delete),
                              ),
                            ],
                          ),
                        ),
                      )
                    ],
                  );
                },
              ),
            )
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

class Item {
  String key;
  String body;
  String title;
  String type;

  Item(this.body, this.title, this.type);

  Item.fromSnapshot(DataSnapshot snapshot)
      : key = snapshot.key,
        body = snapshot.value["body"],
        title = snapshot.value["title"],
        type = snapshot.value["type"];

  toJson() {
    return {
      "body": body,
      "title": title,
      "type": type,
    };
  }
}
