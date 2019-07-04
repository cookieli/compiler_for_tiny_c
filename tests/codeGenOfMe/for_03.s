.file "tests/codeGenOfMe/for_03.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/for_03.dcf:7:7:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/for_03.dcf:8:11:out of array bound error for a\n"
.LC2:
.string "%d "
.LC3:
.string "\n"
.LC4:
.string "tests/codeGenOfMe/for_03.dcf:13:7:out of array bound error for a\n"
.LC5:
.string "tests/codeGenOfMe/for_03.dcf:13:21:out of array bound error for a\n"
.LC6:
.string "tests/codeGenOfMe/for_03.dcf:13:31:out of array bound error for a\n"
.LC7:
.string "tests/codeGenOfMe/for_03.dcf:13:37:out of array bound error for a\n"
.LC8:
.string "tests/codeGenOfMe/for_03.dcf:13:42:out of array bound error for a\n"
.LC9:
.string "tests/codeGenOfMe/for_03.dcf:13:17:out of array bound error for a\n"
.LC10:
.string "tests/codeGenOfMe/for_03.dcf:14:22:out of array bound error for a\n"
.LC11:
.string "%d "
.LC12:
.string "tests/codeGenOfMe/for_03.dcf:13:57:out of array bound error for a\n"
.LC13:
.string "tests/codeGenOfMe/for_03.dcf:13:67:out of array bound error for a\n"
.LC14:
.string "tests/codeGenOfMe/for_03.dcf:13:73:out of array bound error for a\n"
.LC15:
.string "tests/codeGenOfMe/for_03.dcf:13:78:out of array bound error for a\n"
.LC16:
.string "tests/codeGenOfMe/for_03.dcf:13:53:out of array bound error for a\n"
.LC17:
.string "tests/codeGenOfMe/for_03.dcf:13:53:out of array bound error for a\n"
.LC18:
.string "\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $224, %rsp
movq $7,-216(%rbp)
movq $0,-152(%rbp)
.L3:
movq -152(%rbp),%rax
movq -216(%rbp),%r10
cmpq %r10,%rax
jge .L4
subq $16, %rsp
movq -152(%rbp),%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-224(%rbp)
movq -152(%rbp),%rax
cmpq $7,%rax
jl .L5
.L6:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L7
.L5:
movq -152(%rbp),%rax
cmpq $0,%rax
jge .L7
jmp .L6
.L7:
movq -152(%rbp),%rax
movq -224(%rbp),%r10
movq %r10,-208(%rbp,%rax,8)
movq -152(%rbp),%rax
cmpq $7,%rax
jl .L8
.L9:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L10
.L8:
movq -152(%rbp),%rax
cmpq $0,%rax
jge .L10
jmp .L9
.L10:
movq -152(%rbp),%rax
movq -208(%rbp,%rax,8),%r10
movq %r10,-232(%rbp)
movq -232(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -152(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-152(%rbp)
addq $16, %rsp
jmp .L3
.L4:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $0,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L11
.L12:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L13
.L11:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L13
jmp .L12
.L13:
movq $3,-208(%rbp)
.L14:
movq $2,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L15
.L16:
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L17
.L15:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L17
jmp .L16
.L17:
movq -192(%rbp),%rax
movq %rax,-112(%rbp)
movq -112(%rbp),%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-120(%rbp)
movq $0,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L18
.L19:
leaq .LC6(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L20
.L18:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L20
jmp .L19
.L20:
movq -208(%rbp),%rax
movq %rax,-104(%rbp)
movq -120(%rbp),%rax
movq -104(%rbp),%r10
addq %r10,%rax
movq %rax,-128(%rbp)
movq $1,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L21
.L22:
leaq .LC7(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L23
.L21:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L23
jmp .L22
.L23:
movq -200(%rbp),%rax
movq %rax,-96(%rbp)
movq -128(%rbp),%rax
movq -96(%rbp),%r10
imulq %r10,%rax
movq %rax,-136(%rbp)
movq $4,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L24
.L25:
leaq .LC8(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L26
.L24:
movq $4,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L26
jmp .L25
.L26:
movq -176(%rbp),%rax
movq %rax,-88(%rbp)
movq -136(%rbp),%rax
movq -88(%rbp),%r10
cltd
idivq %r10
movq %rdx,-144(%rbp)
movq -144(%rbp),%rax
cmpq $7,%rax
jl .L27
.L28:
leaq .LC9(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
.L29:
movq -144(%rbp),%rax
movq -208(%rbp,%rax,8),%r10
movq %r10,-80(%rbp)
movq -80(%rbp),%rax
cmpq $8,%rax
jge .L30
subq $16, %rsp
movq $3,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L31
.L32:
leaq .LC10(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L33
.L31:
movq $3,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L33
jmp .L32
.L33:
movq -184(%rbp),%rax
movq %rax,-224(%rbp)
movq -224(%rbp),%rsi
leaq .LC11(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $2,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L34
.L35:
leaq .LC12(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L36
.L34:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L36
jmp .L35
.L36:
movq -192(%rbp),%rax
movq %rax,-40(%rbp)
movq -40(%rbp),%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-48(%rbp)
movq $0,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L37
.L38:
leaq .LC13(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L39
.L37:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L39
jmp .L38
.L39:
movq -208(%rbp),%rax
movq %rax,-32(%rbp)
movq -48(%rbp),%rax
movq -32(%rbp),%r10
addq %r10,%rax
movq %rax,-56(%rbp)
movq $1,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L40
.L41:
leaq .LC14(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L42
.L40:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L42
jmp .L41
.L42:
movq -200(%rbp),%rax
movq %rax,-24(%rbp)
movq -56(%rbp),%rax
movq -24(%rbp),%r10
imulq %r10,%rax
movq %rax,-64(%rbp)
movq $4,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L43
.L44:
leaq .LC15(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L45
.L43:
movq $4,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L45
jmp .L44
.L45:
movq -176(%rbp),%rax
movq %rax,-16(%rbp)
movq -64(%rbp),%rax
movq -16(%rbp),%r10
cltd
idivq %r10
movq %rdx,-72(%rbp)
movq -72(%rbp),%rax
cmpq $7,%rax
jl .L46
.L47:
leaq .LC16(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L48
.L46:
movq -72(%rbp),%rax
cmpq $0,%rax
jge .L48
jmp .L47
.L48:
movq -72(%rbp),%rax
movq -208(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -72(%rbp),%rax
cmpq $7,%rax
jl .L49
.L50:
leaq .LC17(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L51
.L49:
movq -72(%rbp),%rax
cmpq $0,%rax
jge .L51
jmp .L50
.L51:
movq -72(%rbp),%rax
movq -8(%rbp),%r10
movq %r10,-208(%rbp,%rax,8)
addq $16, %rsp
jmp .L14
.L30:
leaq .LC18(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leave
ret
.L27:
movq -144(%rbp),%rax
cmpq $0,%rax
jge .L29
jmp .L28
jmp .L29

