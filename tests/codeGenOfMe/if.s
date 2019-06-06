.file "tests/codeGenOfMe/if.dcf"
.text
.section .rodata
.LC0:
.string "%d\n"
.LC1:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $4,-16(%rbp)
movq $5,-8(%rbp)
leave
ret

