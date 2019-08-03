.file "tests/DataFlow/for.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
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
movq $3,-16(%rbp)
.L2:
movq -16(%rbp),%rax
cmpq $6,%rax
jge .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -16(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
movq %rax,-8(%rbp)
jmp .L2
.L1:
leave
ret

