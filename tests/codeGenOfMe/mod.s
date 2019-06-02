.file "tests/codeGenOfMe/mod.dcf"
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
movq $4,-16(%rbp)
movq $2,-8(%rbp)
movq -16(%rbp), %rax
movq $2, %r10
cltd
idivq %r10
movq %rdx, -8(%rbp)
movq -8(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

