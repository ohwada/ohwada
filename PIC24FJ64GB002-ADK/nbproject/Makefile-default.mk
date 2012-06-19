#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Include project Makefile
include Makefile
# Include makefile containing local settings
ifeq "$(wildcard nbproject/Makefile-local-default.mk)" "nbproject/Makefile-local-default.mk"
include nbproject/Makefile-local-default.mk
endif

# Environment
MKDIR=mkdir -p
RM=rm -f 
MV=mv 
CP=cp 

# Macros
CND_CONF=default
ifeq ($(TYPE_IMAGE), DEBUG_RUN)
IMAGE_TYPE=debug
OUTPUT_SUFFIX=cof
DEBUGGABLE_SUFFIX=cof
FINAL_IMAGE=dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${OUTPUT_SUFFIX}
else
IMAGE_TYPE=production
OUTPUT_SUFFIX=hex
DEBUGGABLE_SUFFIX=cof
FINAL_IMAGE=dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${OUTPUT_SUFFIX}
endif

# Object Directory
OBJECTDIR=build/${CND_CONF}/${IMAGE_TYPE}

# Distribution Directory
DISTDIR=dist/${CND_CONF}/${IMAGE_TYPE}

# Object Files Quoted if spaced
OBJECTFILES_QUOTED_IF_SPACED=${OBJECTDIR}/Common/uart2.o ${OBJECTDIR}/USB/usb_host.o ${OBJECTDIR}/USB/usb_host_android.o ${OBJECTDIR}/USB/usb_host_android_protocol_v1.o ${OBJECTDIR}/main.o ${OBJECTDIR}/usb_config.o
POSSIBLE_DEPFILES=${OBJECTDIR}/Common/uart2.o.d ${OBJECTDIR}/USB/usb_host.o.d ${OBJECTDIR}/USB/usb_host_android.o.d ${OBJECTDIR}/USB/usb_host_android_protocol_v1.o.d ${OBJECTDIR}/main.o.d ${OBJECTDIR}/usb_config.o.d

# Object Files
OBJECTFILES=${OBJECTDIR}/Common/uart2.o ${OBJECTDIR}/USB/usb_host.o ${OBJECTDIR}/USB/usb_host_android.o ${OBJECTDIR}/USB/usb_host_android_protocol_v1.o ${OBJECTDIR}/main.o ${OBJECTDIR}/usb_config.o


CFLAGS=
ASFLAGS=
LDLIBSOPTIONS=

############# Tool locations ##########################################
# If you copy a project from one host to another, the path where the  #
# compiler is installed may be different.                             #
# If you open this project with MPLAB X in the new host, this         #
# makefile will be regenerated and the paths will be corrected.       #
#######################################################################
# fixDeps replaces a bunch of sed/cat/printf statements that slow down the build
FIXDEPS=fixDeps

.build-conf:  ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-default.mk dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${OUTPUT_SUFFIX}

MP_PROCESSOR_OPTION=24FJ64GB002
MP_LINKER_FILE_OPTION=,--script=p24FJ64GB002.gld
# ------------------------------------------------------------------------------------
# Rules for buildStep: compile
ifeq ($(TYPE_IMAGE), DEBUG_RUN)
${OBJECTDIR}/Common/uart2.o: Common/uart2.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/Common 
	@${RM} ${OBJECTDIR}/Common/uart2.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  Common/uart2.c  -o ${OBJECTDIR}/Common/uart2.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/Common/uart2.o.d"    -g -D__DEBUG -D__MPLAB_DEBUGGER_PK3=1  -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/Common/uart2.o.d" $(SILENT) 
	
${OBJECTDIR}/USB/usb_host.o: USB/usb_host.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/USB 
	@${RM} ${OBJECTDIR}/USB/usb_host.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  USB/usb_host.c  -o ${OBJECTDIR}/USB/usb_host.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/USB/usb_host.o.d"    -g -D__DEBUG -D__MPLAB_DEBUGGER_PK3=1  -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/USB/usb_host.o.d" $(SILENT) 
	
${OBJECTDIR}/USB/usb_host_android.o: USB/usb_host_android.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/USB 
	@${RM} ${OBJECTDIR}/USB/usb_host_android.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  USB/usb_host_android.c  -o ${OBJECTDIR}/USB/usb_host_android.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/USB/usb_host_android.o.d"    -g -D__DEBUG -D__MPLAB_DEBUGGER_PK3=1  -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/USB/usb_host_android.o.d" $(SILENT) 
	
${OBJECTDIR}/USB/usb_host_android_protocol_v1.o: USB/usb_host_android_protocol_v1.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/USB 
	@${RM} ${OBJECTDIR}/USB/usb_host_android_protocol_v1.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  USB/usb_host_android_protocol_v1.c  -o ${OBJECTDIR}/USB/usb_host_android_protocol_v1.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/USB/usb_host_android_protocol_v1.o.d"    -g -D__DEBUG -D__MPLAB_DEBUGGER_PK3=1  -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/USB/usb_host_android_protocol_v1.o.d" $(SILENT) 
	
${OBJECTDIR}/main.o: main.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR} 
	@${RM} ${OBJECTDIR}/main.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  main.c  -o ${OBJECTDIR}/main.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/main.o.d"    -g -D__DEBUG -D__MPLAB_DEBUGGER_PK3=1  -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/main.o.d" $(SILENT) 
	
${OBJECTDIR}/usb_config.o: usb_config.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR} 
	@${RM} ${OBJECTDIR}/usb_config.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  usb_config.c  -o ${OBJECTDIR}/usb_config.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/usb_config.o.d"    -g -D__DEBUG -D__MPLAB_DEBUGGER_PK3=1  -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/usb_config.o.d" $(SILENT) 
	
else
${OBJECTDIR}/Common/uart2.o: Common/uart2.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/Common 
	@${RM} ${OBJECTDIR}/Common/uart2.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  Common/uart2.c  -o ${OBJECTDIR}/Common/uart2.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/Common/uart2.o.d"    -g -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/Common/uart2.o.d" $(SILENT) 
	
${OBJECTDIR}/USB/usb_host.o: USB/usb_host.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/USB 
	@${RM} ${OBJECTDIR}/USB/usb_host.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  USB/usb_host.c  -o ${OBJECTDIR}/USB/usb_host.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/USB/usb_host.o.d"    -g -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/USB/usb_host.o.d" $(SILENT) 
	
${OBJECTDIR}/USB/usb_host_android.o: USB/usb_host_android.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/USB 
	@${RM} ${OBJECTDIR}/USB/usb_host_android.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  USB/usb_host_android.c  -o ${OBJECTDIR}/USB/usb_host_android.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/USB/usb_host_android.o.d"    -g -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/USB/usb_host_android.o.d" $(SILENT) 
	
${OBJECTDIR}/USB/usb_host_android_protocol_v1.o: USB/usb_host_android_protocol_v1.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR}/USB 
	@${RM} ${OBJECTDIR}/USB/usb_host_android_protocol_v1.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  USB/usb_host_android_protocol_v1.c  -o ${OBJECTDIR}/USB/usb_host_android_protocol_v1.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/USB/usb_host_android_protocol_v1.o.d"    -g -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/USB/usb_host_android_protocol_v1.o.d" $(SILENT) 
	
${OBJECTDIR}/main.o: main.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR} 
	@${RM} ${OBJECTDIR}/main.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  main.c  -o ${OBJECTDIR}/main.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/main.o.d"    -g -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/main.o.d" $(SILENT) 
	
${OBJECTDIR}/usb_config.o: usb_config.c  nbproject/Makefile-${CND_CONF}.mk
	@${MKDIR} ${OBJECTDIR} 
	@${RM} ${OBJECTDIR}/usb_config.o.d 
	${MP_CC} $(MP_EXTRA_CC_PRE)  usb_config.c  -o ${OBJECTDIR}/usb_config.o  -c -mcpu=$(MP_PROCESSOR_OPTION)  -MMD -MF "${OBJECTDIR}/usb_config.o.d"    -g -omf=coff -O0 -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/Common" -I"/Users/ohwada/MPLABXProjects/PIC24FJ64GB002-ADK.X/USB" -msmart-io=1 -Wall -msfr-warn=off
	@${FIXDEPS} "${OBJECTDIR}/usb_config.o.d" $(SILENT) 
	
endif

# ------------------------------------------------------------------------------------
# Rules for buildStep: assemble
ifeq ($(TYPE_IMAGE), DEBUG_RUN)
else
endif

# ------------------------------------------------------------------------------------
# Rules for buildStep: assemblePreproc
ifeq ($(TYPE_IMAGE), DEBUG_RUN)
else
endif

# ------------------------------------------------------------------------------------
# Rules for buildStep: link
ifeq ($(TYPE_IMAGE), DEBUG_RUN)
dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${OUTPUT_SUFFIX}: ${OBJECTFILES}  nbproject/Makefile-${CND_CONF}.mk   
	@${MKDIR} dist/${CND_CONF}/${IMAGE_TYPE} 
	${MP_CC} $(MP_EXTRA_LD_PRE)  -o dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${OUTPUT_SUFFIX}  ${OBJECTFILES_QUOTED_IF_SPACED}      -mcpu=$(MP_PROCESSOR_OPTION)    -D__DEBUG -D__MPLAB_DEBUGGER_PK3=1  -omf=coff -Wl,--defsym=__MPLAB_BUILD=1,--defsym=__MPLAB_DEBUG=1,--defsym=__ICD2RAM=1,--defsym=__DEBUG=1,--defsym=__MPLAB_DEBUGGER_PK3=1,$(MP_LINKER_FILE_OPTION),--heap=3000,--stack=16,--check-sections,--data-init,--pack-data,--handles,--isr,--no-gc-sections,--fill-upper=0,--stackguard=16,--no-force-link,--smart-io,--report-mem$(MP_EXTRA_LD_POST) 
	
else
dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${OUTPUT_SUFFIX}: ${OBJECTFILES}  nbproject/Makefile-${CND_CONF}.mk   
	@${MKDIR} dist/${CND_CONF}/${IMAGE_TYPE} 
	${MP_CC} $(MP_EXTRA_LD_PRE)  -o dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${DEBUGGABLE_SUFFIX}  ${OBJECTFILES_QUOTED_IF_SPACED}      -mcpu=$(MP_PROCESSOR_OPTION)    -omf=coff -Wl,--defsym=__MPLAB_BUILD=1,$(MP_LINKER_FILE_OPTION),--heap=3000,--stack=16,--check-sections,--data-init,--pack-data,--handles,--isr,--no-gc-sections,--fill-upper=0,--stackguard=16,--no-force-link,--smart-io,--report-mem$(MP_EXTRA_LD_POST) 
	${MP_CC_DIR}/xc16-bin2hex dist/${CND_CONF}/${IMAGE_TYPE}/PIC24FJ64GB002-ADK.X.${IMAGE_TYPE}.${DEBUGGABLE_SUFFIX} -a  -omf=coff 
	
endif


# Subprojects
.build-subprojects:


# Subprojects
.clean-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r build/default
	${RM} -r dist/default

# Enable dependency checking
.dep.inc: .depcheck-impl

DEPFILES=$(shell "${PATH_TO_IDE_BIN}"mplabwildcard ${POSSIBLE_DEPFILES})
ifneq (${DEPFILES},)
include ${DEPFILES}
endif
