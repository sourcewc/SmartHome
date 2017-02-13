var exec = require('child_process').exec;
var child = exec('./source/get_temper.elf', (err, temper) => {
	if(temper > 30){
	temper = 30;
}else if(temper < 10){
	temper = 10;
}

    var child2 = exec('python send.py ' + temper, (err, stdout) => {
        setTimeout(function () {
            var child3 = exec('node schedule.js');
        }, 60000);
	console.log('message send....');
    });
});
