// 2012-06-10 PIC24FJ64GB002

/********************************************************************
 FileName:     	PIC24FJ64GB002_PIM.h
 Dependencies:  See INCLUDES section
 Processor:     PIC24FJ64GB002
 Hardware:      PIC24FJ64GB002 PIM
 Compiler:      Microchip C30
 Company:       Microchip Technology, Inc.

 Software License Agreement:

 The software supplied herewith by Microchip Technology Incorporated
 (the ?Company?) for its PIC? Microcontroller is intended and
 supplied to you, the Company?s customer, for use solely and
 exclusively on Microchip PIC Microcontroller products. The
 software is owned by the Company and/or its supplier, and is
 protected under applicable copyright laws. All rights are reserved.
 Any use in violation of the foregoing restrictions may subject the
 user to criminal sanctions under applicable laws, as well as to
 civil liability for the breach of the terms and conditions of this
 license.

 THIS SOFTWARE IS PROVIDED IN AN ?AS IS? CONDITION. NO WARRANTIES,
 WHETHER EXPRESS, IMPLIED OR STATUTORY, INCLUDING, BUT NOT LIMITED
 TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 PARTICULAR PURPOSE APPLY TO THIS SOFTWARE. THE COMPANY SHALL NOT,
 IN ANY CIRCUMSTANCES, BE LIABLE FOR SPECIAL, INCIDENTAL OR
 CONSEQUENTIAL DAMAGES, FOR ANY REASON WHATSOEVER.

********************************************************************
 File Description:

 Change History:
  Rev   Date         Description
        2012-06-10   Initial support for PIC24FJ64GB002 family
********************************************************************/

// PIC24FJ64GB002
//#ifndef HARDWARE_PROFILE_PIC24FJ64GB004_PIM_H
//#define HARDWARE_PROFILE_PIC24FJ64GB004_PIM_H
#ifndef HARDWARE_PROFILE_PIC24FJ64GB002_PIM_H
#define HARDWARE_PROFILE_PIC24FJ64GB002_PIM_H

    /*******************************************************************/
    /*******************************************************************/
    /*******************************************************************/
    /******** Application specific definitions *************************/
    /*******************************************************************/
    /*******************************************************************/
    /*******************************************************************/

    /** Board definition ***********************************************/
    //These defintions will tell the main() function which board is
    //  currently selected.  This will allow the application to add
    //  the correct configuration bits as wells use the correct
    //  initialization functions for the board.  These defitions are only
    //  required in the stack provided demos.  They are not required in
    //  final application design.

// PIC24FJ64GB002    
//  #define DEMO_BOARD PIC24FJ64GB004_PIM
    #define DEMO_BOARD PIC24FJ64GB002_PIM
    #define EXPLORER_16
//  #define PIC24FJ64GB004_PIM
    #define PIC24FJ64GB002_PIM
    #define CLOCK_FREQ 32000000

// PIC24FJ64GB002  
//  #define DEMO_BOARD_NAME_STRING "PIC24FJ64GB004 PIM"
    #define DEMO_BOARD_NAME_STRING "PIC24FJ64GB002 PIM"
        
    /** LED ************************************************************/
// PIC24FJ64GB002
//    #define InitAllLEDs()      LATA &= 0xFD7F; TRISA &= 0xFD7F; LATB &= 0xFFF3; TRISB &= 0xFFF3;
    
//    #define mLED_1              LATAbits.LATA7
//    #define mLED_2              LATBbits.LATB3
//    #define mLED_3              LATBbits.LATB2
//    #define mLED_4              LATAbits.LATA9     
    
    #define LED0_On()         mLED_1 = 1;
    #define LED1_On()         mLED_2 = 1;
    #define LED2_On()         mLED_3 = 1;
    #define LED3_On()         mLED_4 = 1;
//    #define LED4_On()
//    #define LED5_On()
//    #define LED6_On()
//    #define LED7_On()
    
    #define LED0_Off()        mLED_1 = 0;
    #define LED1_Off()        mLED_2 = 0;
    #define LED2_Off()        mLED_3 = 0;
    #define LED3_Off()        mLED_4 = 0;
//    #define LED4_Off()
//    #define LED5_Off()
//    #define LED6_Off()
//    #define LED7_Off()

// PIC24FJ64GB002
// set output RB15, 14, 13, 9, 8, 7, 5, 4
    #define InitAllLEDs()      LATB &= 0x1C4F; TRISB &= 0x1C4F;
      
    #define mLED_1            LATBbits.LATB4
    #define mLED_2            LATBbits.LATB5
    #define mLED_3            LATBbits.LATB7
    #define mLED_4            LATBbits.LATB8
    #define mLED_5            LATBbits.LATB9
    #define mLED_6            LATBbits.LATB13
    #define mLED_7            LATBbits.LATB14
    #define mLED_8            LATBbits.LATB15
    
    #define LED4_On()         mLED_5 = 1;
    #define LED5_On()         mLED_6 = 1;
    #define LED6_On()         mLED_7 = 1;
    #define LED7_On()         mLED_8 = 1;
    
    #define LED4_Off()         mLED_5 = 0;
    #define LED5_Off()         mLED_6 = 0;
    #define LED6_Off()         mLED_7 = 0;
    #define LED7_Off()         mLED_8 = 0;
       
    /** SWITCH *********************************************************/
// PIC24FJ64GB002    
//    #define mInitSwitch2()      TRISAbits.TRISA10=1;
//    #define mInitSwitch3()      TRISAbits.TRISA9=1;
//    #define InitAllSwitches()  mInitSwitch2();mInitSwitch3();
//    #define sw2                 PORTAbits.RA10
//    #define sw3                 PORTAbits.RA9

//    #define Switch1Pressed()    ((PORTAbits.RA10  == 0)? TRUE : FALSE)
//    #define Switch2Pressed()    ((PORTAbits.RA9  == 0)? TRUE : FALSE)
//    #define Switch3Pressed()    FALSE
//    #define Switch4Pressed()    FALSE

// PIC24FJ64GB002
// set input RA4, 3, 2, 1 
    #define mInitSwitch1()      TRISAbits.TRISA1=1;
    #define mInitSwitch2()      TRISAbits.TRISA2=1;
    #define mInitSwitch3()      TRISAbits.TRISA3=1;
    #define mInitSwitch4()      TRISAbits.TRISA4=1;
    #define InitAllSwitches()    mInitSwitch1(); mInitSwitch2(); mInitSwitch3(); mInitSwitch4();

    #define Switch1Pressed()    ((PORTAbits.RA1  == 0)? TRUE : FALSE)
    #define Switch2Pressed()    ((PORTAbits.RA2  == 0)? TRUE : FALSE)
    #define Switch3Pressed()    ((PORTAbits.RA3  == 0)? TRUE : FALSE)
    #define Switch4Pressed()    ((PORTAbits.RA4  == 0)? TRUE : FALSE)

    /** POT ************************************************************/
    #define mInitPOT()  {AD1PCFGLbits.PCFG7 = 0;    AD1CON2bits.VCFG = 0x0;    AD1CON3bits.ADCS = 0xFF;    AD1CON1bits.SSRC = 0x0;    AD1CON3bits.SAMC = 0b10000;    AD1CON1bits.FORM = 0b00;    AD1CON2bits.SMPI = 0x0;    AD1CON1bits.ADON = 1;}

    /** I/O pin definitions ********************************************/
    #define INPUT_PIN 1
    #define OUTPUT_PIN 0

    /** Debug print interface ******************************************/
    #define DEBUG_Init(a)
    #define DEBUG_Error(a)          
    #define DEBUG_PrintString(a)    
    #define DEBUG_PrintHex(a)

#endif  //HARDWARE_PROFILE_PIC24FJ64GB002_PIM_H
