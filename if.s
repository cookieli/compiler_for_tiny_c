.file "tests/codeGenOfMe/ternary.dcf"
.text
.section .rodata
.LC0:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,%rax
movq %rax,-16(%rbp)
movq %rax,-8(%rbp)
movb $1,-9(%rbp)
movb -9(%rbp),%al
cmpb $0,%al
jle .L2
movq $4,-8(%rbp)
jmp .L3
.L2:
movq $5,-8(%rbp)
.L3:
movq -8(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L1:
leave
ret

