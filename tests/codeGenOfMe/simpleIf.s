.file "tests/codeGenOfMe/simpleIf.dcf"
.text
.section .rodata
.LC0:
.string "hello world"
.LC1:
.string "fuck the world"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $2,-16(%rbp)
movq $3,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

