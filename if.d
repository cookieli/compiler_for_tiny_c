.file "tests/DataFlow/constant.dcf"
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
subq $48, %rsp
movq $0,%rax
movq %rax,-48(%rbp)
movq %rax,-40(%rbp)
movq %rax,-32(%rbp)
movq %rax,-24(%rbp)
movq %rax,-16(%rbp)
movq %rax,-8(%rbp)
movq $0,-48(%rbp)
movq $4,-40(%rbp)
movq $0,-32(%rbp)
movq $2,-16(%rbp)
movq -8(%rbp),%rax
cmpq $0,%rax
jne .L2
movq $1,-24(%rbp)
jmp .L3
.L2:
movq $2,-24(%rbp)
.L3:
nop
.L4:
movq -32(%rbp),%rax
movq -16(%rbp),%r10
cmpq %r10,%rax
jge .L5
subq $32, %rsp
movq $0,%rax
movq %rax,(%rsp)
movq %rax,8(%rsp)
movq %rax,16(%rsp)
movq %rax,24(%rsp)
movq $4,%rax
movq -24(%rbp),%r10
imulq %r10,%rax
movq %rax,-72(%rbp)
movq -48(%rbp),%rax
movq -72(%rbp),%r10
addq %r10,%rax
movq %rax,-48(%rbp)
movq -32(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-32(%rbp)
addq $16, %rsp
jmp .L4
.L5:
movq -48(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L1:
leave
ret

