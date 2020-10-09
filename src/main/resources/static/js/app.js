let app = (() => {
    let cinemaName = undefined;
    let date = undefined;
    let movies = []
    let service = apiclient;
    let stompClient = null;
    class Seat {
        constructor(row, col) {
            this.row = row;
            this.col = col;
        }
    };
    // let service = apimock;
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
                } else if (j >= 2 && j < 10) {
                    lapiz.fillRect(x + 48, y, 30, 30);
                } else {
                    lapiz.fillRect(x + 72, y, 30, 30);
                }
            }
        }
        lapiz.fillStyle = 'darkSlateGray';
        lapiz.fillRect(canvas.width - 390, canvas.height - 30, 275, 6);
        $('#numeroDeAsientos').text(`Asientos Disponibles: ${numeroAsientosDisponibles}`);
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
                    draw(funcion.seats);
                    break;
                }
            }
        })
    }
    const getMousePosition = function (evt) {
        $('#canvas').click(function (e) {
            var rect = canvas.getBoundingClientRect();
            var x = e.clientX - rect.left;
            var y = e.clientY - rect.top;
            console.log(x,y);
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
    const buyTicket = (row, col, cinema, date, movieName) => {
        service.buyTicket(cinema, date, movieName, row - 1, col - 1, (funcion) => {
                draw(funcion.seats)
            }
        )
    }
    const deleteFunction = (cinema,date,movieName) =>{
        console.log("Hola?")
        clearCanvas();
        const availability = $('#Availability');
        availability.empty();
        service.deleteFunction(cinema,date,movieName, () =>{
            getFunctions(cinema,date);
        })
    }
    const connectAndSubscribe = (id) =>{
        $('#canvas').off("click");
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/buyticket.'+id, message =>{
                    let response = JSON.parse(message.body);
                    app.changeSeat(response.row,response.col);
                }
            );
        });
    };
    const changeSeat = (row, col) => {
        let st = new Seat(row, col);
    }
    const addEventListener = () =>{
        getMousePosition();
        document.getElementById("buyTicket").disabled = true;
    };
    return {
        idCinema:'',
        changeCinemaName:changeDate,
        changeDate:changeDate,
        getSeats:getSeats,
        admin:admin,
        createOrUpdateFunction:createOrUpdateFunction,
        buyTicket:buyTicket,
        getFunctions:getFunctions,
        nuevaFuncion: nuevaFuncion,
        deleteFunction:deleteFunction,
        changeSeat:changeSeat,
        addEventListener:addEventListener,
        connectToMovie(id){
            this.idCinema = id;
            connectAndSubscribe(id);
        }

    }

})();
