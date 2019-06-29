.file "tests/codeGenOfMe/continue_04.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/continue_04.dcf:8:7:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/continue_04.dcf:11:7:out of array bound error for a\n"
.LC2:
.string "tests/codeGenOfMe/continue_04.dcf:11:20:out of array bound error for a\n"
.LC3:
.string "tests/codeGenOfMe/continue_04.dcf:13:11:out of array bound error for a\n"
.LC4:
.string "tests/codeGenOfMe/continue_04.dcf:16:11:out of array bound error for a\n"
.LC5:
.string "tests/codeGenOfMe/continue_04.dcf:19:15:out of array bound error for a\n"
.LC6:
.string "%d\n"
.LC7:
.string "tests/codeGenOfMe/continue_04.dcf:11:34:out of array bound error for a\n"
.LC8:
.string "tests/codeGenOfMe/continue_04.dcf:11:34:out of array bound error for a\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $144, %rsp
movq $8,-144(%rbp)
movq $2,-72(%rbp)
movq $1,-64(%rbp)
movq $0,-56(%rbp)
.L1:
movq -56(%rbp),%rax
movq -144(%rbp),%r10
cmpq %r10,%rax
jge .L2
movq -56(%rbp),%rax
cmpq $8,%rax
jl .L3
.L4:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L5
.L3:
movq -56(%rbp),%rax
cmpq $0,%rax
jge .L5
jmp .L4
.L5:
movq -56(%rbp),%rax
movq -56(%rbp),%r10
movq %r10,-136(%rbp,%rax,8)
movq -56(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-56(%rbp)
jmp .L1
.L2:
movq -72(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-48(%rbp)
movq -48(%rbp),%rax
cmpq $8,%rax
jl .L6
.L7:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L8
.L6:
movq -48(%rbp),%rax
cmpq $0,%rax
jge .L8
jmp .L7
.L8:
movq -48(%rbp),%rax
movq $10,-136(%rbp,%rax,8)
.L9:
movq -72(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-40(%rbp)
movq -40(%rbp),%rax
cmpq $8,%rax
jl .L10
.L11:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
.L12:
movq -40(%rbp),%rax
movq -136(%rbp,%rax,8),%r10
movq %r10,-32(%rbp)
movq -32(%rbp),%rax
cmpq $100,%rax
jge .L13
subq $48, %rsp
movq -72(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-184(%rbp)
movq -184(%rbp),%rax
cmpq $8,%rax
jl .L14
.L15:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L16
.L14:
movq -184(%rbp),%rax
cmpq $0,%rax
jge .L16
jmp .L15
.L16:
movq -184(%rbp),%rax
movq -136(%rbp,%rax,8),%r10
movq %r10,-176(%rbp)
movq -176(%rbp),%rax
cmpq $80,%rax
jge .L17
movq -72(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-24(%rbp)
movq -24(%rbp),%rax
cmpq $8,%rax
jl .L18
.L19:
leaq .LC7(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L20
.L18:
movq -24(%rbp),%rax
cmpq $0,%rax
jge .L20
jmp .L19
.L20:
movq -24(%rbp),%rax
movq -136(%rbp,%rax,8),%r10
movq %r10,-16(%rbp)
movq -64(%rbp),%rax
movq $5,%r10
imulq %r10,%rax
movq %rax,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq -24(%rbp),%rax
cmpq $8,%rax
jl .L21
.L22:
leaq .LC8(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L23
.L21:
movq -24(%rbp),%rax
cmpq $0,%rax
jge .L23
jmp .L22
.L23:
movq -24(%rbp),%rax
movq -16(%rbp),%r10
movq %r10,-136(%rbp,%rax,8)
nop
jmp .L12
jmp .L17
.L17:
movq -72(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-168(%rbp)
movq -168(%rbp),%rax
cmpq $8,%rax
jl .L24
.L25:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L26
.L24:
movq -168(%rbp),%rax
cmpq $0,%rax
jge .L26
jmp .L25
.L26:
movq -168(%rbp),%rax
movq -136(%rbp,%rax,8),%r10
movq %r10,-160(%rbp)
movq -160(%rbp),%rax
cmpq $95,%rax
jl .L27
nop
jmp .L13
jmp .L27
.L27:
movq -72(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-152(%rbp)
movq -152(%rbp),%rax
cmpq $8,%rax
jl .L28
.L29:
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L30
.L28:
movq -152(%rbp),%rax
cmpq $0,%rax
jge .L30
jmp .L29
.L30:
movq -152(%rbp),%rax
movq -136(%rbp,%rax,8),%r10
movq %r10,-192(%rbp)
movq -192(%rbp),%rsi
leaq .LC6(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -72(%rbp),%rax
movq -64(%rbp),%r10
addq %r10,%rax
movq %rax,-24(%rbp)
movq -24(%rbp),%rax
cmpq $8,%rax
jl .L31
.L32:
leaq .LC7(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L33
.L31:
movq -24(%rbp),%rax
cmpq $0,%rax
jge .L33
jmp .L32
.L33:
movq %rax,%rax
movq -136(%rbp,%rax,8),%r10
movq %r10,-16(%rbp)
movq -64(%rbp),%rax
movq $5,%r10
imulq %r10,%rax
movq %rax,-8(%rbp)
movq -16(%rbp),%rax
movq -8(%rbp),%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq -24(%rbp),%rax
cmpq $8,%rax
jl .L34
.L35:
leaq .LC8(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L36
.L34:
movq -24(%rbp),%rax
cmpq $0,%rax
jge .L36
jmp .L35
.L36:
movq %rax,%rax
movq -16(%rbp),%r10
movq %r10,-136(%rbp,%rax,8)
addq $48, %rsp
jmp .L9
.L13:
leave
ret
.L10:
movq -40(%rbp),%rax
cmpq $0,%rax
jge .L12
jmp .L11
jmp .L12

