.file "tests/codeGenOfMe/for_02.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/for_02.dcf:5:7:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/for_02.dcf:5:17:out of array bound error for a\n"
.LC2:
.string "%d "
.LC3:
.string "tests/codeGenOfMe/for_02.dcf:5:28:out of array bound error for a\n"
.LC4:
.string "tests/codeGenOfMe/for_02.dcf:5:28:out of array bound error for a\n"
.LC5:
.string "\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $2,-40(%rbp)
movq $1,%rax
movq $2,%r10
cmpq %r10,%rax
jl .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L3
.L1:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L3
jmp .L2
.L3:
movq $0,-24(%rbp)
.L4:
movq $1,%rax
movq $2,%r10
cmpq %r10,%rax
jl .L5
.L6:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
.L7:
movq -24(%rbp),%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
cmpq $10,%rax
jge .L8
subq $16, %rsp
movq $100,-48(%rbp)
movq -48(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $1,%rax
movq $2,%r10
cmpq %r10,%rax
jl .L9
.L10:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L11
.L9:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L11
jmp .L10
.L11:
movq -24(%rbp),%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq $1,%rax
movq $2,%r10
cmpq %r10,%rax
jl .L12
.L13:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L14
.L12:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L14
jmp .L13
.L14:
movq -8(%rbp),%rax
movq %rax,-24(%rbp)
addq $16, %rsp
jmp .L4
.L8:
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret
.L5:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L7
jmp .L6
jmp .L7

