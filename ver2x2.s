
shades = 13

lo_bits_mod0 = shadebase+ 0*shades
lo_bits_mod1 = shadebase+ 1*shades
hi_bits_mod0 = shadebase+ 2*shades
hi_bits_mod1 = shadebase+ 3*shades


.proc   PreparePatterns

        ldx #(shades-1)

c1:     lda shade_mod0,x
        sta lo_bits_mod0,x

        lda shade_mod1,x
        sta lo_bits_mod1,x
        dex
        bpl c1

        ldx #(2*shades-1)

c2:     ldy shadebase,x
        lda mul16,y
        sta shadebase+2*shades,x

        dex
        bpl c2
        rts

mul16: .byte $00,$10,$20,$30,$40,$50,$60,$70,$80,$90,$a0,$b0,$c0,$d0,$e0,$f0

.endproc


.proc   CopyPatternsMod0

        ldy #0

c:      ldx powers,y

        lda hi_bits_mod0,x
        sta hi_bits,y
        and #%11000000
        sta tup3bits,y
        eor hi_bits_mod0,x
        sta tup2bits,y

        lda lo_bits_mod0,x
        sta lo_bits,y
        and #%1100
        sta tup1bits,y
        eor lo_bits_mod0,x
        sta tup0bits,y

        ldx powers+256,y

        lda hi_bits_mod0,x
        sta hi_bits+256,y
        and #%11000000
        sta tup3bits+256,y
        eor hi_bits_mod0,x
        sta tup2bits+256,y

        lda lo_bits_mod0,x
        sta lo_bits+256,y
        and #%1100
        sta tup1bits+256,y
        eor lo_bits_mod0,x
        sta tup0bits+256,y

        iny
        bne c
        rts

.endproc


.proc   CopyPatternsMod1

        ldy #0

c:      ldx powers,y

        lda hi_bits_mod1,x
        sta hi_bits,y
        and #%11000000
        sta tup3bits,y
        eor hi_bits_mod1,x
        sta tup2bits,y

        lda lo_bits_mod1,x
        sta lo_bits,y
        and #%1100
        sta tup1bits,y
        eor lo_bits_mod1,x
        sta tup0bits,y

        ldx powers+256,y

        lda hi_bits_mod1,x
        sta hi_bits+256,y
        and #%11000000
        sta tup3bits+256,y
        eor hi_bits_mod1,x
        sta tup2bits+256,y

        lda lo_bits_mod1,x
        sta lo_bits+256,y
        and #%1100
        sta tup1bits+256,y
        eor lo_bits_mod1,x
        sta tup0bits+256,y

        iny
        bne c
        rts

.endproc

