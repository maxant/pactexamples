'use strict';

const PORT = 8091;

const express = require('express');
const app = express();

app.post('/all', (req, res) => {
    console.log("post/all");
    res.status(200).send([
               {
                 "title": "My first event",
                 "location": "Eversons Fry House",
                 "date": "2017-02-17T21:00:00.000"
               },
               {
                 "title": "My second event",
                 "location": "Shakin Pancakes",
                 "date": "2017-02-18T22:00:00.000"
               },
               {
                 "title": "A third event",
                 "location": "Dories Donuts",
                 "date": "2017-02-19T09:00:00.000"
               }
             ]
    );
});

app.post('/dictionary', (req, res) => {
    console.log("post/dictionary");
    res.status(200).send(
        {
            "ant": {
                "name": "ant",
                "number": 4
            },
            "john": {
                "name": "john",
                "number": 6
            }
        }
    );
});

//handle errors
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).send('Something broke!');
});

//start server
app.listen(PORT, () => {
    console.log('events listening on port ' + PORT);
});
