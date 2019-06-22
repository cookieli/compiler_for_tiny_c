.file "tests/codeGenOfMe/while_05.dcf"
.text
.section .rodata
.LC0:
.string "hello world %d\n"
.LC1:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $160, %rsp
movq $4,-160(%rbp)
movq $1,-152(%rbp)
movq $6,-144(%rbp)
movq $3,-136(%rbp)
movq $5,-120(%rbp)
movq $6,-112(%rbp)
movq $8,-104(%rbp)
movq $2,-96(%rbp)
.L1:
movq -120(%rbp),%rax
movq -112(%rbp),%r10
addq %r10,%rax
movq %rax,-72(%rbp)
movq -104(%rbp),%rax
movq -96(%rbp),%r10
addq %r10,%rax
movq %rax,-64(%rbp)
movq -72(%rbp),%rax
movq -64(%rbp),%r10
subq %r10,%rax
movq %rax,-80(%rbp)
movq -152(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-56(%rbp)
movq -80(%rbp),%rax
movq -56(%rbp),%r10
imulq %r10,%rax
movq %rax,-88(%rbp)
movq -88(%rbp),%rax
movq -152(%rbp,%rax,8),%r10
movq %r10,-48(%rbp)
movq -136(%rbp),%rax
movq %rax,-40(%rbp)
movq -152(%rbp),%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-32(%rbp)
movq -128(%rbp),%rax
movq %rax,-24(%rbp)
movq -136(%rbp),%rax
movq %rax,-16(%rbp)
movq -144(%rbp),%rax
movq $3,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -48(%rbp),%rax
cmpq $6,%rax
jge .L2
movq -40(%rbp),%rax
movq -32(%rbp),%r10
cmpq %r10,%rax
jl .L2
.L3:
subq $16, %rsp
movq -136(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -136(%rbp),%rax
movq %rax,-176(%rbp)
movq -176(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-176(%rbp)
movq -176(%rbp),%rax
movq %rax,-136(%rbp)
movq -136(%rbp),%rax
movq $4,%r10
addq %r10,%rax
movq %rax,-168(%rbp)
movq -168(%rbp),%rax
movq %rax,-128(%rbp)
addq $16, %rsp
jmp .L1
.L2:
movq -24(%rbp),%rax
cmpq $20,%rax
jge .L4
jmp .L3
.L4:
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jge .L5
jmp .L3
.L5:
movq -128(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

