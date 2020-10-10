let app = (() => {
    let cinemaName = undefined;
    let date = undefined;
    let movies = []
    let service = apiclient;
    let seats = [];
    let seatPositions = [[null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null], [null, null, null, null, null, null, null, null, null, null, null, null]];
    let stompClient = null;
    class Seat {
        constructor(row, col) {
            this.row = row;
            this.col = col;
        }
    };
    class SeatPosition {
        constructor(row, col) {
            this.x = row;
            this.y = col;
        }
    };
    const mapToObjects = (cinemaFunctions) => {
        let table = $("#tabla > tbody");
        table.empty();
        movies = cinemaFunctions.map(({movie: {name, genre}, date}) => ({  //retorna un objeto sin necesidad de return :3
            name: name,
            genre: genre,
            date: date,
            hour: date.split(" ")[1]
        }));
        movies.forEach(({name, genre, hour,date}) => {
            table.append(
                `<tr> <td>${name}</td>
                      <td>${genre}</td>
                      <td>${hour}</td>
                      <td>
                          <button type="button" onclick="app.getSeats($('#cinema').val(),
                              $('#date').val().concat(' ','${hour}'),'${name}');
                                app.connectToMovie($('#cinema').val().concat('.')
                                .concat('${date}').concat('.').concat('${name}'))" 
                              class="btn btn-primary">Open seats
                          </button>
                      </td>
                      <td>
                          <button type="button" onclick="app.admin($('#cinema').val(),
                              $('#date').val(),'${name}','${genre}')" 
                              class="btn btn-primary">Administrar
                          </button>
                      </td>
                </tr>`
            );
        })
    }
    const clearCanvas = () =>{
        let canvas = document.getElementById('canvas');
        let lapiz = canvas.getContext("2d");
        lapiz.fillStyle = "white";
        lapiz.fillRect(0, 0, canvas.width, canvas.height);
    }
    const draw = (seats) => {
        let numeroAsientosDisponibles = 0;
        let canvas = document.getElementById('canvas');
        let lapiz = canvas.getContext("2d");
        clearCanvas();
        lapiz.strokeStyle = 'lightgrey';
        for (let i = 0; i < 7; i++) {
            for (let j = 0; j < 12; j++) {
                if (seats[i][j] === true) {
                    numeroAsientosDisponibles++;
                    lapiz.fillStyle = "#BDC3C7";
                } else {
                    lapiz.fillStyle = "#2980B9";
                }
                const x = j * 35, y = i * 35
                if (j < 2) {
                    lapiz.fillRect(x + 24, y, 30, 30);
                    setPosition(i,j,x+24,y);
                } else if (j >= 2 && j < 10) {
                    lapiz.fillRect(x + 48, y, 30, 30);
                    setPosition(i,j,x+48,y);
                } else {
                    lapiz.fillRect(x + 72, y, 30, 30);
                    setPosition(i,j,x+72,y)
                }
            }
        }
        lapiz.fillStyle = 'darkSlateGray';
        lapiz.fillRect(canvas.width - 390, canvas.height - 30, 275, 6);
        $('#numeroDeAsientos').text(`Asientos Disponibles: ${numeroAsientosDisponibles}`);
    }
    const setPosition = (i,j,x,y)  => {
        if(seatPositions[i][j] === null){
            seatPositions[i][j] = new SeatPosition(x, y);
        }
    }
    const changeCinemaName = (newCinema) =>{
        cinemaName = newCinema;
    }
    const changeDate = (newDate) => {
        date = newDate;
    }

    const nuevaFuncion = () =>{
        let controls = $("#nuevaFuncion");
        controls.empty()
        clearCanvas()
        controls.append(
            `<label>Nombre</label>
             <input type="text" id="inputName" placeholder="Nombre" name="Fila">
             <label>Genero</label>
             <input type="text" id="inputGenre" placeholder="Genero" name="Columna">    
            `
        )
    }
    const getFunctions = (cinema, date) => {
        changeCinemaName(cinema);
        changeDate(date);
        $('#seleccionado').text(`Cine seleccionado: ${cinemaName}`);
        service.getFunctionsByCinemaAndDate(cinema, date, mapToObjects)
    }
    const getAvalability = (name, date, functionName) =>{
        let availability = $('#Availability');
        availability.empty();
        availability.append(`Availability ${functionName}`)
        $('#nuevaFuncion').empty();
        service.getFunctionsByCinemaAndDate(name, date, (funciones) => {
            for (const funcion of funciones) {
                if (funcion.movie.name === functionName) {
                    seats = funcion.seats;
                    draw(funcion.seats);
                    break;
                }
            }
        })
    }
    const checkPosition = (x, y) => {
        console.log("hola?");
        console.log(seatPositions.length);
        for (let i=0; i<seatPositions.length;i++){
            for(let j=0; j<seatPositions[i].length;j++){
                if (x >= seatPositions[i][j].x && x <= seatPositions[i][j].x + 30) {
                    if (y >= seatPositions[i][j].y && y <= seatPositions[i][j].y + 30) {
                        app.buyTicket(i,j)
                    }
                }
            }
        }
    };
    const getMousePosition = function (evt) {
        $('#canvas').click(function (e) {
            var rect = canvas.getBoundingClientRect();
            var x = e.clientX - rect.left;
            var y = e.clientY - rect.top;
            checkPosition(x, y);
        });
    };
    const getSeats = (name, date, functionName) => {
        let controls = $("#controls");
        controls.empty();
        controls.append(
            `<label>Comprar ticket</label>
                    <button type="button" class="btn btn-warning" 
                    onclick="app.addEventListener()" id="buyTicket">Comprar</button>
                `
        );
        getAvalability(name, date, functionName);
    }
    const admin = (name, date, functionName, genre) =>{
        let controls = $("#controls");
        controls.empty()
        controls.append(
            `<div class="container" id="admin">
                    <h2>Admin mode</h2>
                    <form class="form-inline">
                        <label for="edit"> Hora:</label>
                        <input type="time" id="edit" placeholder="Nueva hora" name="edit">
                            <button type="button" onclick="app.createOrUpdateFunction('${name}','${date}'.concat(' ',$('#edit').val()),'${functionName}','${genre}')"
                                    class="btn btn-warning">Guardar/Actualizar
                            </button>
                    </form>
                    <form class="form-inline">
                        <button type="button" onclick="app.nuevaFuncion()"
                                class="btn btn-warning">Nueva función
                        </button>
                        <button type="button" onclick="app.deleteFunction('${name}','${date}','${functionName}')"
                                class="btn btn-warning">Eliminar función
                        </button>
                    </form>
            </div>`
        );
        getAvalability(name,date,functionName);
    }
    const createOrUpdateFunction = (cinema, date, functionName, genre) => {
        const inputName = $('#inputName').val();
        const inputGenre = $('#inputGenre').val();
        if((inputName === undefined && inputGenre === undefined)||(inputName==='' && inputGenre==='')){
            console.log("Si actualice")
            const functionData = {"movie":{"name":functionName,"genre":genre},"seats":[],"date":date}
            service.updateFunction(cinema,functionData,(funcion)=>{
                getFunctions(cinema,funcion.date.split(" ")[0]);
            });
        }else{
            const functionData =  {"movie":{"name":inputName,"genre":inputGenre},"seats":[],"date":date}
            service.createFunction(cinema,functionData,(funcion)=>{
                console.log(funcion.seats)
                getFunctions(cinema,funcion.date.split(" ")[0]);
            });
        }
    }
    const deleteFunction = (cinema,date,movieName) =>{
        clearCanvas();
        const availability = $('#Availability');
        availability.empty();
        service.deleteFunction(cinema,date,movieName, () =>{
            getFunctions(cinema,date);
        })
    }
    const connectAndSubscribe = (id) =>{
        $('#canvas').off("click");
        app.disconnect();
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/buyticket.'+id, message =>{
                    let response = JSON.parse(message.body);
                    draw(response);
                }
            );
        });
    };

    const addEventListener = () =>{
        getMousePosition();
        document.getElementById("buyTicket").disabled = true;
    };
    const verifyAvailability = function (row,col,id) {
        let seat = new Seat(row,col)
        if (seats[row][col] === true){
            seats[row][col]=false;
            stompClient.send("/app/buyticket." + id, {} , JSON.stringify(seat));
        }
        else{
            alert("Ticket not available");
        }
        stompClient.send("/app/buyticket." + id, {} , JSON.stringify(seat));
    };
    return {
        idCinema:'',
        changeCinemaName:changeDate,
        changeDate:changeDate,
        getSeats:getSeats,
        admin:admin,
        createOrUpdateFunction:createOrUpdateFunction,
        getFunctions:getFunctions,
        nuevaFuncion: nuevaFuncion,
        deleteFunction:deleteFunction,
        addEventListener:addEventListener,
        connectToMovie(id){
            this.idCinema = id;
            connectAndSubscribe(id);
        },
        buyTicket: function (row, col) {
            let st = new Seat(row, col);
            console.info("buying ticket at row: " + row + "col: " + col);
            verifyAvailability(row,col,this.idCinema);
        },disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            console.log("Disconnected");
        }

    }

})();
