.file "tests/codeGenOfMe/simpleAdd.dcf"
.text
.section .rodata
.LC0:
.string "the sum of %d and %d is %d"
.LC1:
.string "%d"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $2,-32(%rbp)
movq $6,-24(%rbp)
movq -32(%rbp), %rax
movq -24(%rbp), %r10
addq %r10, %rax
movq %rax, -16(%rbp)
movq -32(%rbp), %rax
movq -24(%rbp), %r10
imulq %r10, %rax
movq %rax, -8(%rbp)
movq -16(%rbp),%rcx
movq -24(%rbp),%rdx
movq -32(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -8(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

