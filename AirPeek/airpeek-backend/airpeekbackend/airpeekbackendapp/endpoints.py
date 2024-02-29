import json
import secrets
import bcrypt
import datetime
from django.core.exceptions import PermissionDenied
from django.db.models import F, ExpressionWrapper, FloatField
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils import timezone
from .models import User, UserSession, Flights, UserFlights


def authenticate_user(request):
    session_token = request.headers.get('SessionToken', None)
    # Extract the token from the request headers
    if session_token is None:
        raise PermissionDenied('Unauthorized')
    # Authenticate user based on the provided token
    try:
        user_session = UserSession.objects.get(token=session_token)
    except UserSession.DoesNotExist:
        raise PermissionDenied('Unauthorized')
    return user_session


def check_email_format(email):
    if email is None or '@' not in email or len(email) < 8:
        return False
    return True


def check_password_format(password):
    if len(password) < 8:
        return False
    return True


def check_image_format(image):
    if image is not None and ('http://' not in image and 'https://' not in image) and (
            '.jpg' not in image and '.png' not in image and '.webp' not in image):
        return False
    return True


def special_offers(request):
    if request.method == 'GET':
        json_response = []
        offers = Flights.objects.filter(departure_date__gte=timezone.now()).annotate(
            duration=F('arrival_date') - F('departure_date'),
            profitability=ExpressionWrapper(F('price') / F('duration'), output_field=FloatField())
        ).order_by('profitability')[:20]
        for flights in offers:
            json_response.append(flights.to_json())

        if not offers:
            return JsonResponse({'response': 'Offer not found'}, status=404)

        return JsonResponse(json_response, safe=False, status=200)
    else:
        return JsonResponse({'response': 'Invalid request method'}, status=405)


def flights(request):
    if request.method == 'GET':
        try:
            client_arrival_datetime = request.GET.get('arrival_date')
            client_departure_datetime = request.GET.get('departure_date')
            client_departure_location = request.GET.get('origin')
            client_arrival_location = request.GET.get('destination')
        except KeyError:
            return JsonResponse({"response": "not_ok"}, status=400)

        dep_date = datetime.datetime.strptime(client_departure_datetime, "%Y-%m-%d %H:%M:%S").date()

        json_response = {
            "cheapest": [],
            "fastest": []
        }

        if client_arrival_datetime is None:

            cheapest_flights = Flights.objects.filter(origin=client_departure_location,
                                                      destination=client_arrival_location,
                                                      departure_date__date=dep_date).order_by('price')[:3]
            for flights in cheapest_flights:
                json_response["cheapest"].append(flights.to_json())

            fastest_flights = Flights.objects.annotate(duration=F('arrival_date') - F('departure_date')).filter(
                origin=client_departure_location, destination=client_arrival_location,
                departure_date__date=dep_date).order_by('duration')[:3]
            for flight in fastest_flights:
                json_response["fastest"].append(flight.to_json())

            try:
                cheapest_flights = Flights.objects.filter(origin=client_departure_location,
                                                          destination=client_arrival_location,
                                                          departure_date__date=dep_date).order_by('price')[:3]
                fastest_flights = Flights.objects.annotate(duration=F('arrival_date') - F('departure_date')).filter(
                    origin=client_departure_location, destination=client_arrival_location,
                    departure_date__date=dep_date).order_by('duration')[:3]
            except Flights.DoesNotExist:
                return JsonResponse({"response": "not_ok"}, status=404)
            
            if len(cheapest_flights) < 3 or len(fastest_flights) < 3:
                return JsonResponse({"response": "not_ok"}, status=404)

            return JsonResponse(json_response, safe=False, status=200)

        else:
            arr_date = datetime.datetime.strptime(client_arrival_datetime, "%Y-%m-%d %H:%M:%S").date()

            cheapest_flights_departure = Flights.objects.filter(origin=client_departure_location,
                                                                destination=client_arrival_location,
                                                                departure_date__date=dep_date).order_by('price')[:2]
            cheapest_flights_arrival = Flights.objects.filter(origin=client_arrival_location,
                                                              destination=client_departure_location,
                                                              departure_date__date=arr_date).order_by('price')[:1]

            for i in range(max(len(cheapest_flights_departure), len(cheapest_flights_arrival))):
                if i < len(cheapest_flights_departure):
                    json_response["cheapest"].append(cheapest_flights_departure[i].to_json())
                if i < len(cheapest_flights_arrival):
                    json_response["cheapest"].append(cheapest_flights_arrival[i].to_json())

            fastest_flights_departure = Flights.objects.annotate(
                duration=F('arrival_date') - F('departure_date')).filter(origin=client_departure_location,
                                                                         destination=client_arrival_location,
                                                                         departure_date__date=dep_date).order_by(
                'duration')[:2]
            fastest_flights_arrival = Flights.objects.annotate(duration=F('departure_date') - F('arrival_date')).filter(
                origin=client_arrival_location, destination=client_departure_location,
                departure_date__date=arr_date).order_by('duration')[:1]

            for i in range(max(len(fastest_flights_departure), len(fastest_flights_arrival))):
                if i < len(fastest_flights_departure):
                    json_response["fastest"].append(fastest_flights_departure[i].to_json())
                if i < len(fastest_flights_arrival):
                    json_response["fastest"].append(fastest_flights_arrival[i].to_json())

            try:
                cheapest_flights_departure = Flights.objects.filter(origin=client_departure_location,
                                                                    destination=client_arrival_location,
                                                                    departure_date__date=dep_date)
                cheapest_flights_arrival = Flights.objects.filter(origin=client_arrival_location,
                                                                  destination=client_departure_location,
                                                                  departure_date__date=arr_date)
                fastest_flights_departure = Flights.objects.annotate(
                    duration=F('arrival_date') - F('departure_date')).filter(origin=client_departure_location,
                                                                             destination=client_arrival_location,
                                                                             departure_date__date=dep_date)
                fastest_flights_arrival = Flights.objects.annotate(
                    duration=F('departure_date') - F('arrival_date')).filter(origin=client_arrival_location,
                                                                             destination=client_departure_location,
                                                                             departure_date__date=arr_date)
            except Flights.DoesNotExist:
                return JsonResponse({"response": "not_ok"}, status=404)
            if len(cheapest_flights_departure) < 2 or len(cheapest_flights_arrival) < 1 or len(fastest_flights_departure) < 2 or len(fastest_flights_arrival) < 1:
                return JsonResponse({"response": "not_ok"}, status=404)

            return JsonResponse(json_response, safe=False, status=200)
    else:
        return JsonResponse({'response': 'Invalid request method'}, status=405)


@csrf_exempt
def user(request):
    if request.method == 'POST':  # Register a new user
        try:
            client_json = json.loads(request.body)
            client_name = client_json['name']
            client_email = client_json['email']
            client_password = client_json['password']
            client_country = client_json['country']
            client_birthdate = client_json['birthdate']
        except KeyError:
            return JsonResponse({"response": "not_ok"}, status=400)
        if client_email is None or '@' not in client_email or len(client_email) < 8:
            return JsonResponse({"response": "not_ok"}, status=400)
        try:
            User.objects.get(email=client_email)
            return JsonResponse({"response": "already_exist"}, status=409)
        except:
            pass
        salted_and_hashed_pass = bcrypt.hashpw(client_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
        new_user = User(name=client_name, email=client_email, password=salted_and_hashed_pass, country=client_country,
                        birthdate=client_birthdate)
        new_user.save()
        return JsonResponse({"response": "ok"}, status=201)
    elif request.method == 'GET':
        try:
            user_session = authenticate_user(request)
            user_id = user_session.user.id
            full_user = User.objects.get(id=user_id)
            json_response = full_user.to_json()
            return JsonResponse(json_response, safe=False, status=200)
        except PermissionDenied:
            return JsonResponse({'error': 'Unauthorized'}, status=401)
    elif request.method == 'PUT':
        try:
            user_session = authenticate_user(request)
        except PermissionDenied:
            return JsonResponse({'error': 'Unauthorized'}, status=401)
        try:
            body_json = json.loads(request.body)
            name = body_json.get('name', None)
            email = body_json.get('email', None)
            password = body_json.get('password', None)
            image = body_json.get('image', None)
        except KeyError:
            return JsonResponse({"response": "not_ok"}, status=401)
        if not (check_email_format(email) and check_password_format(password) and check_image_format(image)):
            return JsonResponse({"response": "not_ok"}, status=400)
        if email in User.objects.get(id=user_session.user.id).email:
            return JsonResponse({"response": "already_exist"}, status=409)
        salted_and_hashed_pass = bcrypt.hashpw(password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
        user = User.objects.get(id=user_session.user.id)
        user.name = name
        user.email = email
        user.password = salted_and_hashed_pass
        user.image = image
        user.save()
        return JsonResponse({"response": "ok"}, status=200)
    elif request.method == 'DELETE':
        try:
            user_session = authenticate_user(request)
        except PermissionDenied:
            return JsonResponse({'error': 'Unauthorized'}, status=401)
        try:
            user = User.objects.get(id=user_session.user.id)
            user.delete()
            return JsonResponse({'response': 'ok'}, status=200)
        except User.DoesNotExist:
            return JsonResponse({'response': 'not_okay'}, status=500)
    else:
        return JsonResponse({"response": "Method Not Allowed"}, status=405)


def user_flights(request):
    if request.method == 'GET':  ## Empleamos GET para obtener todos los vuelos que tiene el usuario guardados.
        user_session = authenticate_user(request)  ## Esta función es para autenticar al user y permitirle acceso.
        if not user_session:
            return JsonResponse({'response': 'Unauthorized'}, status=401)
        user = user_session.user_id  ## Guardamos en una variable el ID del user una vez que la hayamos en la session.
        u_flights = UserFlights.objects.filter(user=user)  ## Comprobamos los vuelos asociados al user, si es que hay.
        json_response = []
        for u_flight in u_flights:
            flight = u_flight.flight
            ## Comprobamos los ID de los vuelos que haya en la tabla flight_id para leer el resto de los datos.
            if flight is None:
                return JsonResponse({'response': 'Offer not found.'}, status=404)
            flights_dict = {
                'flight_id': flight.flight,
                'departure_location': flight.origin,
                'arrival_location': flight.destination,
                'departure_datetime': flight.departure_date.isoformat(),
                'arrival_datetime': flight.arrival_date.isoformat(),
                'price': str(flight.price),
                'buyUrl': flight.buyUrl,
            }
            json_response.append(flights_dict)
        return JsonResponse(json_response, safe=False)


@csrf_exempt
def user_flightsdetail(request, flight):
    if request.method == 'DELETE':
        try:
            user_session = authenticate_user(request)
        except PermissionDenied:
            return JsonResponse({'response': 'Unauthorized'}, status=401)
        try:
            flightcheck = Flights.objects.get(flight=flight)  # Add this line
        except Flights.DoesNotExist:
            return JsonResponse({'response': 'Flight not found'}, status=404)
        try:
            user_flight = UserFlights.objects.get(flight=flight, user=user_session.user)
        except UserFlights.DoesNotExist:
            return JsonResponse({'response': 'Flight cannot be removed'}, status=404)
        user_flight.delete()
        return JsonResponse({'response': 'Flight deleted'}, status=200)
    elif request.method == 'PUT':  ## Un user añade un vuelo a su cuenta.
        try:
            user_session = authenticate_user(request)
        except PermissionDenied:
            return JsonResponse({'response': 'Unauthorized'}, status=401)
        user = user_session.user
        try:  ## Ahora, según el ID del vuelo, lo buscamos en la tabla de vuelos. En caso de que no exista, se lanza 404
            flight = Flights.objects.get(flight=flight)
        except Flights.DoesNotExist:
            return JsonResponse({'response': 'Not Found'}, status=404)
        ## Una vez hallado el vuelo, lo añadimos a UserFlights, relacionándolo con el usuario en cuestión.
        user_flight, created = UserFlights.objects.get_or_create(user=user, flight=flight)

        if created:
            # Si el UserFlight fue creado, es porque no existía previamente.
            return JsonResponse({'response': 'ok'}, status=201, safe=False)
        else:
            # Si el UserFlight no fue creado, es porque ya existía.
            return JsonResponse({'response': 'Flight already added to user'}, status=409)
    else:
        return JsonResponse({'response': 'Method Not Allowed'},
                            status=405)  ## Si no fuera este método, no estaría permitido.


@csrf_exempt
def sessions(request):
    if request.method == 'POST':
        try:
            client_json = json.loads(request.body)
            json_email = client_json['email']
            json_password = client_json['password']
        except KeyError:
            return JsonResponse({"response": "not_ok"}, status=400)
        try:
            db_user = User.objects.get(email=json_email)
        except User.DoesNotExist:
            return JsonResponse({"response": "User not in database"}, status=404)  # No existe el usuario
        if bcrypt.checkpw(json_password.encode('utf8'), db_user.password.encode('utf8')):
            # json_password y db_user.encrypted_password coinciden
            random_token = secrets.token_hex(10)
            session = UserSession(user=db_user, token=random_token)
            session.save()
            return JsonResponse({"response": "ok", "SessionToken": random_token}, status=201)
        else:
            # No coinciden
            return JsonResponse({"response": "Unauthorized"}, status=401)
    elif request.method == 'DELETE':
        try:
            user_session = authenticate_user(request)
        except PermissionDenied:
            return JsonResponse({'response': 'Unauthorized'}, status=401)
        user_session.delete()
        return JsonResponse({"response": "Sesión cerrada"}, status=201, safe=False)
    else:
        return JsonResponse({"response": "HTTP method unsupported"}, status=405)
