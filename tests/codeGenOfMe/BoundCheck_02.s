.file "tests/codeGenOfMe/BoundCheck_02.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/BoundCheck_02.dcf:13:6:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/BoundCheck_02.dcf:13:4:out of array bound error for a\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $192, %rsp
movq $9,-184(%rbp)
movq $100,-104(%rbp)
movq $20,-96(%rbp)
movq $3,-88(%rbp)
movq $80,-80(%rbp)
movq $2,-72(%rbp)
movq -96(%rbp),%rax
movq -88(%rbp),%r10
imulq %r10,%rax
movq %rax,-24(%rbp)
movq -88(%rbp),%rax
movq -72(%rbp),%r10
subq %r10,%rax
movq %rax,-16(%rbp)
movq -24(%rbp),%rax
movq -16(%rbp),%r10
cqto
idivq %r10
movq %rax,-32(%rbp)
movq -104(%rbp),%rax
movq -32(%rbp),%r10
addq %r10,%rax
movq %rax,-40(%rbp)
movq -104(%rbp),%rax
movq -80(%rbp),%r10
cltd
idivq %r10
movq %rdx,-8(%rbp)
movq -40(%rbp),%rax
movq -8(%rbp),%r10
addq %r10,%rax
movq %rax,-48(%rbp)
movq -48(%rbp),%rax
cmpq $9,%rax
jl .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L1
.L1:
movq -48(%rbp),%rax
movq -176(%rbp,%rax,8),%r10
movq %r10,-56(%rbp)
movq -56(%rbp),%rax
cmpq $9,%rax
jl .L2
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L2
.L2:
movq -56(%rbp),%rax
movq $20,-176(%rbp,%rax,8)
leave
ret

