.file "tests/codeGenOfMe/add.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d, %d, %d, %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $80, %rsp
movq $8,-80(%rbp)
movq $2,-72(%rbp)
movq $5,-64(%rbp)
movq $3,-56(%rbp)
movq -80(%rbp),%rax
movq -72(%rbp),%r10
addq %r10,%rax
movq %rax,-32(%rbp)
movq -32(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-40(%rbp)
movq -40(%rbp),%rax
movq -56(%rbp),%r10
subq %r10,%rax
movq %rax,-48(%rbp)
movq -80(%rbp),%rax
movq -72(%rbp),%r10
imulq %r10,%rax
movq %rax,-16(%rbp)
movq $4,%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cqto
idivq %r10
movq %rax,-24(%rbp)
movq -48(%rbp),%rax
movq -24(%rbp),%r10
subq %r10,%rax
movq %rax,-56(%rbp)
movq $8,%rdi
pushq %rdi
movq $1,%rdi
pushq %rdi
movq $1,%r9
movq -56(%rbp),%r8
movq -64(%rbp),%rcx
movq -72(%rbp),%rdx
movq -80(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
addq $16,%rsp
movq $0,%rax
leave
ret

