.file "tests/codeGenOfMe/if_with_def.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/if_with_def.dcf:6:6:out of array bound error for c\n"
.LC1:
.string "tests/codeGenOfMe/if_with_def.dcf:7:6:out of array bound error for c\n"
.LC2:
.string "tests/codeGenOfMe/if_with_def.dcf:8:6:out of array bound error for c\n"
.LC3:
.string "tests/codeGenOfMe/if_with_def.dcf:9:6:out of array bound error for c\n"
.LC4:
.string "tests/codeGenOfMe/if_with_def.dcf:10:13:out of array bound error for c\n"
.LC5:
.string "tests/codeGenOfMe/if_with_def.dcf:10:18:out of array bound error for c\n"
.LC6:
.string "tests/codeGenOfMe/if_with_def.dcf:10:6:out of array bound error for c\n"
.LC7:
.string "tests/codeGenOfMe/if_with_def.dcf:13:9:out of array bound error for c\n"
.LC8:
.string "a is %d\n"
.LC9:
.string "a is %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $96, %rsp
movq $4,-72(%rbp)
movq $0,%rax
movq $4,%r10
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
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L3
jmp .L2
.L3:
movq $1,-64(%rbp)
movq $1,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L4
.L5:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L6
.L4:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L6
jmp .L5
.L6:
movq $2,-56(%rbp)
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L7
.L8:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L9
.L7:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L9
jmp .L8
.L9:
movq $3,-48(%rbp)
movq $3,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L10
.L11:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L12
.L10:
movq $3,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L12
jmp .L11
.L12:
movq $5,-40(%rbp)
movq $0,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L13
.L14:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L15
.L13:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L15
jmp .L14
.L15:
movq -64(%rbp),%rax
movq %rax,-24(%rbp)
movq $1,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L16
.L17:
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L18
.L16:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L18
jmp .L17
.L18:
movq -56(%rbp),%rax
movq %rax,-16(%rbp)
movq -24(%rbp),%rax
movq -16(%rbp),%r10
imulq %r10,%rax
movq %rax,-32(%rbp)
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L19
.L20:
leaq .LC6(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L21
.L19:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L21
jmp .L20
.L21:
movq -32(%rbp),%rax
movq %rax,-48(%rbp)
movq $9,-88(%rbp)
movq $10,-80(%rbp)
movq $1,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L22
.L23:
leaq .LC7(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L24
.L22:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L24
jmp .L23
.L24:
movq -56(%rbp),%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rax
cmpq $10,%rax
jge .L25
subq $16, %rsp
movq $10,-104(%rbp)
movq $11,-96(%rbp)
movq -104(%rbp),%rsi
leaq .LC8(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $16, %rsp
jmp .L25
.L25:
movq -88(%rbp),%rsi
leaq .LC9(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

