var app2 = (function () {
    let seats = [[true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true], [true, true, true, true, true, true, true, true, true, true, true, true]];
    let seatPositions = [[null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null]];
    class Seat {
        constructor(row, col) {
            this.row = row;
            this.col = col;
        }
    };
    class SeatPosition{
        constructor(x,y) {
            this.x = x;
            this.y = y;
        }
    };
    var stompClient = null;
    const checkPosition = (x, y) => {
        for (let i = 0; i < seatPositions.length; i++) {
            for (let j = 0; j < seatPositions[i].length; j++) {
                if (x >= seatPositions[i][j].x && x <= seatPositions[i][j].x + 20) {
                    if (y >= seatPositions[i][j].y && y <= seatPositions[i][j].y + 20) {
                        app2.buyTicket(i,j)
                    }
                }
            }
        }
    };
    const getMousePosition = function (evt) {
        $('#myCanvas').click(function (e) {
            var rect = myCanvas.getBoundingClientRect();
            var x = e.clientX - rect.left;
            var y = e.clientY - rect.top;
            checkPosition(x, y);
        });
    };
    const drawSeats = function (cinemaFunction) {
        var c = document.getElementById("myCanvas");
        var ctx = c.getContext("2d");
        ctx.fillStyle = "#001933";
        ctx.fillRect(100, 20, 300, 80);
        ctx.fillStyle = "#FFFFFF";
        ctx.font = "40px Arial";
        ctx.fillText("Screen", 180, 70);
        var row = 5;
        var col = 0;
        for (var i = 0; i < seats.length; i++) {
            row++;
            col = 0;
            for (j = 0; j < seats[i].length; j++) {
                if (seats[i][j]) {
                    ctx.fillStyle = "#009900";
                } else {
                    ctx.fillStyle = "#FF0000";
                }
                col++;
                ctx.fillRect(20 * col, 20 * row, 20, 20);
                if (seatPositions[i][j] === null){
                    seatPositions[i][j] = new SeatPosition(20 * col, 20 * row);
                }
                col++;
            }
            row++;
        }
    };
    var connectAndSubscribe = function (id) {
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/buyticket.'+id, message =>{
                let response = JSON.parse(message.body);
                app2.changeSeat(response.row,response.col);
            }
            );
        });
    };
    let addEventListener = () =>{
        getMousePosition();
        document.getElementById("buyTicket").disabled = true;
    };
    let verifyAvailability = function (row,col,id) {
        let seat = new Seat(row,col)
        if (seats[row][col] === true){
            seats[row][col]=false;
            stompClient.send("/app/buyticket." + id, {} , JSON.stringify(seat));
        }
        else{
            alert("Ticket not available");
        }
    };
    return {
        idCinema:'',
        init: function () {
            var can = document.getElementById("canvas");
            drawSeats();
            connectAndSubscribe();
        },
        connectToMovie: function(id){
            var can = document.getElementById("canvas");
            drawSeats();
            this.idCinema = id;
            connectAndSubscribe(id);
        },
        addEventListener:addEventListener,
        buyTicket: function (row, col) {
            let st = new Seat(row, col);
            console.info("buying ticket at row: " + row + "col: " + col);
            verifyAvailability(row,col,this.idCinema);
            drawSeats();
        },
        changeSeat: function (row, col) {
            let st = new Seat(row, col);
            seats[row][col]=false;
            drawSeats();
        },
        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }
    };
})();
