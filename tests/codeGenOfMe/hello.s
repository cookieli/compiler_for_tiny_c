.file "tests/codeGenOfMe/hello.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d, %d, %d, %d, %d\n"
.LC1:
.string "hello world"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $3,-48(%rbp)
movq $4,-40(%rbp)
movq $5,-32(%rbp)
movq -48(%rbp), %rax
movq -40(%rbp), %r10
addq %r10, %rax
movq %rax, -24(%rbp)
movq -48(%rbp), %rax
movq -32(%rbp), %r10
subq %r10, %rax
movq %rax, -16(%rbp)
movq $-30,-8(%rbp)
subq $8,%rsp
movq -8(%rbp),%rdi
pushq %rdi
movq -16(%rbp),%r9
movq -24(%rbp),%r8
movq -32(%rbp),%rcx
movq -40(%rbp),%rdx
movq -48(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
addq $16,%rsp
movq $0,%rax
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

