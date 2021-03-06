import os
import pickle
import csv
from Reservation import Reservation
import BasicMethodsToWorkWith
from datetime import datetime


def saveReservation(obj, filename):
    with open(filename, 'ab') as outp:  # Overwrites any existing file.
        pickle.dump(obj, outp, pickle.HIGHEST_PROTOCOL)


def readReservationsFile():
    if os.path.exists("reservationInfo.pkl"):
        inp = open("reservationInfo.pkl", 'rb')
        objectsRes = []
        cont = 1
        while cont == 1:
            try:
                objectsRes.append(pickle.load(inp))
            except EOFError:
                print()
                cont = 0
        for res1 in objectsRes:
            Reservation.printReservation(res1)
        print("\t--------------------------------")
        print("\tFounded " + str(len(objectsRes)) + " reservations. \n")
    else:
        print("No files founded with that name...")


def deleteReservation():
    if os.path.exists("reservationInfo.pkl"):
        inp = open("reservationInfo.pkl", 'rb')
        objectsRes = []
        cont = 1
        while cont == 1:
            try:
                objectsRes.append(pickle.load(inp))
            except EOFError:
                cont = 0
        print()
        for res in objectsRes:
            Reservation.printReservation(res)

        print("\t--------------------------------")
        resDeleteId = BasicMethodsToWorkWith.BasicsMethods.askinteger("the ID of the desired Reservation")
        inp.close()
        os.remove("reservationInfo.pkl")
        for res in objectsRes:
            if Reservation.getReservationID(res) != resDeleteId:
                saveReservation(res, "reservationInfo.pkl")
        readReservationsFile()
        print()
    else:
        print("No files founded with that name...")


def updateReservation():
    if os.path.exists("reservationInfo.pkl"):
        inp = open("reservationInfo.pkl", 'rb')
        objectsRes = []
        cont = 1
        while cont == 1:
            try:
                objectsRes.append(pickle.load(inp))
            except EOFError:
                cont = 0
        print()
        for res in objectsRes:
            Reservation.printReservation(res)
        print("\t--------------------------------")
        desiredResUpdateId = BasicMethodsToWorkWith.BasicsMethods.askinteger("the ID of the desired Reservation")
        inp.close()
        os.remove("reservationInfo.pkl")
        for res in objectsRes:
            if Reservation.getReservationID(res) != desiredResUpdateId:
                saveReservation(res, "reservationInfo.pkl")
            else:
                Reservation.setDate(res)
                Reservation.printReservation(res)
                saveReservation(res, "reservationInfo.pkl")
        readReservationsFile()
        print()
    else:
        print("No files founded with that name...")

