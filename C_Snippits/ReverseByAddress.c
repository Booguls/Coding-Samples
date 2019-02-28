#include <stdio.h>
#include <stdlib.h>
#include <time.h>

//This program reverses an array's sequence of integers by using the address of each element.
//The program populates array 1 with random numbers, array 2 with the addresses of array 1, and array 3 with array 1 in reverse using addresses.

void fillRand();
void storeAddress();
void reverseArr();
void printArrs();

void main(void) {
        srand(time(NULL));		//Use time to declare a seed for randomness.
        int rand[10] = {0};		//Declare an array of ints for random numbers.
        void *add[10] = {0};	//Declare an array of addresses for the first array elements.
        int reverse[10] = {0};	//Declare an array of ints to store the reverse of the first array.
        puts("Step one: Store Values");
        fillRand(rand);			//Call fillRand to fill the first array with random numbers.
        printArrs(rand, add, reverse);	//Print the three arrays.
        puts("\nStep two: Store Addresses");
        storeAddress(rand, add);	//Call storeAddress to store the addresses of each element in array 1 in array 2.
        printArrs(rand, add, reverse);
        puts("\nStep three: Reverse Array 1 using Addresses in Array 2");
        reverseArr(add, reverse);	//Call reverseArr to reverse array 1 elements and store them in array 3 through the use of addresses in array 2.
        printArrs(rand, add, reverse);
}

void fillRand(int arr[]) {	//Function to populate the array with numbers from 0 to 19.
        int i;
        for(i = 0; i < 10; i++) {
                arr[i] = rand() % 20;
        }
}

void storeAddress(int arr[], void *arr2[]) {	//Function to populate array 2 with address of array 1 element locations.
        int i;
        for(i = 0; i < 10; i++) {
                arr2[i] = &arr[i];
        }
}

void reverseArr(char *arr2[], int arr3[]) {		//Function to reverse elements of array 1 by using array 2 addresses.
        int i, k = 9;
        for(i = 0; i < 10; i++) {
                arr3[i] = *arr2[k];
                k--;
        }
}

void printArrs(int arr[], void *arr2[], int arr3[]) {	//Function to print all 3 arrays in each step of the program.
        int i;
        for(i = 0; i < 10; i++) {
                printf("arr1[%d] = %d\n", i, arr[i]);
        }

        puts("<------>");
        for(i = 0; i < 10; i++) {
                printf("arr2[%d] = %p\n", i, arr2[i]);
        }

        puts("<------>");
        for(i = 0; i < 10; i++) {
                printf("arr3[%d] = %d\n", i, arr3[i]);
        }
}
