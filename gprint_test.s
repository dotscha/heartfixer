loadAddr = $1001

.org loadAddr-2
     .word loadAddr

;BASIC PRG
       .word bend
       .byte $d1,$07,$9e
       .byte "4109"
bend:  .byte 0,0,0

.proc   main

        sei
        jsr startOut

        jsr grClear
        jsr grInit


        lda #<page0
        sta sp
        lda #>page0
        sta sp+1

c:      jsr showPage
        dec pages
        bne c

        jmp *
.endproc

pages: .byte 6

.include "gprint.s"

