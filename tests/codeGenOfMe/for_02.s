.file "tests/codeGenOfMe/for_02.dcf"
.text
.section .rodata
.LC0:
.string "%d "
.LC1:
.string "\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $2,-40(%rbp)
movq $0,-24(%rbp)
.L1:
movq -24(%rbp),%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
cmpq $10,%rax
jge .L2
movq $100,-48(%rbp)
movq -48(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -24(%rbp),%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
movq %rax,-24(%rbp)
jmp .L1
.L2:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

