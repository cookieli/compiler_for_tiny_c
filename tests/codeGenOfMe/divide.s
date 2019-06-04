.file "tests/codeGenOfMe/divide.dcf"
.text
.section .rodata
.LC0:
.string "the %d divide %d is %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $6,-24(%rbp)
movq $2,-16(%rbp)
movq -24(%rbp),%rax
movq -16(%rbp),%r10
cqto
idivq %r10
movq %rax,-8(%rbp)
movq -8(%rbp),%rcx
movq -16(%rbp),%rdx
movq -24(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

