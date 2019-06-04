.file "tests/codeGenOfMe/intArith.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $112, %rsp
movq $10,-96(%rbp)
movq $2,-88(%rbp)
movq $3,-80(%rbp)
movq $6,-72(%rbp)
movq $4,-104(%rbp)
movq -104(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-104(%rbp)
movq -96(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-96(%rbp)
movq -104(%rbp),%rax
movq -96(%rbp),%r10
addq %r10,%rax
movq %rax,-104(%rbp)
movq $2,-64(%rbp)
movq -88(%rbp),%rax
movq $2,%r10
imulq %r10,%rax
movq %rax,-32(%rbp)
movq -32(%rbp),%rax
movq $4,%r10
cqto
idivq %r10
movq %rax,-40(%rbp)
movq -96(%rbp),%rax
movq -40(%rbp),%r10
subq %r10,%rax
movq %rax,-48(%rbp)
movq $1,%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq $3,%rax
movq $2,%r10
cltd
idivq %r10
movq %rdx,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
addq %r10,%rax
movq %rax,-24(%rbp)
movq -48(%rbp),%rax
movq -24(%rbp),%r10
addq %r10,%rax
movq %rax,-56(%rbp)
movq -64(%rbp),%rax
movq -56(%rbp),%r10
addq %r10,%rax
movq %rax,-64(%rbp)
movq -64(%rbp),%rcx
movq -96(%rbp),%rdx
movq -104(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

