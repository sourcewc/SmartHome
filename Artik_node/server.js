const express = require('express');
const app = express();
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static('.'));

var exec = require('child_process').exec;
const schedule = require('node-schedule');

const logger = require('morgan');
app.use(logger('dev'));

var request = require("request");

// 불 켜, 불 꺼
// 에어컨 켜, 에어컨 꺼
// 보일러 켜, 보일러 꺼
// 목욕물 켜 (일정 시간 후에 멈춘다)
app.post('/message', (req, res) => {
    const message = req.body['message'];
    console.log(message);
    var orderStr = '';

    switch (message) {
        case '불 켜':
            orderStr = './source/on_led.elf';
            break;
        case '불 꺼':
            orderStr = './source/off_led.elf';
            break;
        case '에어컨 켜':
            console.log('에어컨 켜~');
            break;
        case '에어컨 꺼':
            console.log('에어컨 꺼~');
            break;
        default:
            break;
    }

    var child = exec(orderStr, (err, stdout) => {
        console.log(stdout);
        res.send({ 'message': 1000 });
    });
});

// 조도, 온도 (type)
app.get('/data', (req, res) => {
    var child = exec('./source/get_temper.elf', (err, stdout1) => {
        console.log(stdout1);
        var child2 = exec('./source/get_cds.elf', (err, stdout2) => {
            console.log(stdout2);
            res.send({ 'temper': stdout1, 'cds': stdout2 });
        });
    });
});

app.get('/graph', (req, res) => {

    var options = {
        method: 'GET',
        url: 'https://api.artik.cloud/v1.1/messages',
        qs:
        {
            count: '100',
            endDate: '1486231956149',
            order: 'asc',
            sdid: '725c81264b104e6687b3532b506ef139',
            startDate: '1486228356149',
            uid: 'c33a38a18d1c491f8fbf573e48a9dfc2'
        },
        headers:
        {
            'authorization': 'Bearer ca9bf8f4fbd64672a62cd770b2f41bc1',
            'content-type': 'application/json'
        }
    };

    request(options, function (error, response, body) {
        if (error) throw new Error(error);

        body = JSON.parse(body);
        console.log(body['size']);
        var tempArr = body['data'];
        var temps = [];

        tempArr.forEach((item, idx) => {
            if (item['data']['temp'] < 10) {
                tempArr.slice(idx, 1);
            } else {
                temps.push(item['data']['temp']);
            }
        });

        res.send({ 'size': temps.length, 'temps': temps });
    });
});

app.listen(3001, () => {
    console.log('express server on 3001');
});