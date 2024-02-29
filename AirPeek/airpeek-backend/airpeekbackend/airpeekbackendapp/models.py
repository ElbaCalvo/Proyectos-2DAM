from django.db import models


class User(models.Model):
    image = models.CharField(max_length=100)
    name = models.CharField(max_length=100)
    email = models.EmailField(unique=True, max_length=75)
    country = models.CharField(max_length=100)
    password = models.CharField(max_length=100)
    birthdate = models.DateField()

    def __str__(self):
        return self.name

    def to_json(self):
        return {
            "image": self.image,
            "name": self.name,
            "email": self.email,
            "country": self.country,
            "birthdate": self.birthdate
        }


class Flights(models.Model):
    flight = models.CharField(primary_key=True, max_length=10, unique=True)
    origin = models.CharField(max_length=20)
    destination = models.CharField(max_length=20)
    departure_date = models.DateTimeField()
    arrival_date = models.DateTimeField()
    price = models.DecimalField(max_digits=6, decimal_places=2)  # en centimos por los decimales
    buyUrl = models.URLField(max_length=200, null=True, blank=True)

    def __str__(self):
        return self.flight

    def to_json(self):
        return {
            "id": self.flight,
            "arrival_datetime": self.arrival_date,
            "departure_datetime": self.departure_date,
            "departure_location": self.origin,
            "arrival_location": self.destination,
            "price": self.price,
            "buyUrl": self.buyUrl,
        }


class UserFlights(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    flight = models.ForeignKey(Flights, on_delete=models.CASCADE)

    def __str__(self):
        return self.flight


class UserSession(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    token = models.CharField(unique=True, max_length=45)
