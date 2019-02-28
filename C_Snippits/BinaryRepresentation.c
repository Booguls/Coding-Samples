//Ramirez, Kevin
//CSC 321-01
//4/15/2018
//Professor Malcolm Mcculough

//This program shows the binary representation of an inputted integer in both integer and float types using Union.
//The program demonstrates the difference between struct and union as well as how numbers are stored in float and int notation.

#include <stdio.h>
#include <stdlib.h>

union float_int { //Definition of a union named float_int which contains only type float and type int.
        float f;
        int i;
};

void show_bits();

void main(void) {
        union float_int fi; //Declaration of a union float_int named fi.

        puts("Enter an integer.");
        scanf("%d",&fi.i); //Prompt and store an int in the memory location of float_int integer, then pass to function show_bits().
        show_bits(fi.i);

        puts("Enter a float.");
        scanf("%f",&fi.f); //Prompt and store a float in memory location of float_int float, but pass to function show_bits() as an integer.
        show_bits(fi.i);
}

void show_bits(unsigned int bits) {
        int i;
        for(i = 31; i >= 0; i--) { //For loop which will cycle through the 32-bits of a 32-bit data type.
                int value = bits >> i; //shift the bits of value of bits by the iteration.
                if((value & 1) == 1) { //Bitwise AND determines if the bit shift shifted a 1. If so, print 1. Else print 0.
                        printf("1");
                        if(i % 4 == 0) { //Mod current iteration to check if divisble by four. If so, print a space.
                                printf(" ");
                        }
                }
                else {
                        printf("0");
                        if(i % 4 == 0) {
                                printf(" ");
                        }
                }
        }
        puts("");
}
