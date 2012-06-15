// turn on/off LED using RB15 (26pin)
// tatget: PIC24FJ64GB002
// compiler: XC16

#include "p24FJ64GB002.h"
#include <ports.h>

// CPU clock freq 32MHz
#define CLOCK_FREQ 32000000UL
// loop count per msec. it is adjusted, as it had been 1 msec. 
#define LOOP_COUNT CLOCK_FREQ / 50000UL

_CONFIG1(WDTPS_PS1 & FWPSA_PR32 & WINDIS_OFF & FWDTEN_OFF & ICS_PGx1 & GWRP_OFF & GCP_OFF & JTAGEN_OFF)
_CONFIG2(POSCMOD_HS & I2C1SEL_PRI & IOL1WAY_OFF & OSCIOFNC_ON & FCKSM_CSDCMD & FNOSC_FRCPLL & PLL96MHZ_ON & PLLDIV_DIV2 & IESO_OFF)
_CONFIG3(WPFP_WPFP0 & SOSCSEL_IO & WUTSEL_LEG & WPDIS_WPDIS & WPCFG_WPCFGDIS & WPEND_WPENDMEM)
_CONFIG4(DSWDTPS_DSWDTPS3 & DSWDTOSC_LPRC & RTCOSC_SOSC & DSBOREN_OFF & DSWDTEN_OFF)

void DelayMs(WORD ms)
{
    DWORD dcnt;
    while(ms--) {
        dcnt = LOOP_COUNT;
        while(dcnt--){
            Nop();
        }
    }
}

int main()
{
    mPORTBOutputConfig(IOPORT_BIT_15);
    // 1 sec cycle
    while(1){
        // turn off
        mPORTBWrite(0);
        DelayMs(500);
        // turn on
        mPORTBWrite(IOPORT_BIT_15);
        DelayMs(500);
    }
    return 1;
}
