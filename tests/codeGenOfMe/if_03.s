.file "tests/codeGenOfMe/if_03.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "yes the world\n"
.LC2:
.string "hello world2\n"
.LC3:
.string "hello world %d, %d\n"
.LC4:
.string "la la la %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $10,-41(%rbp)
movq $30,-33(%rbp)
movq $-40,-25(%rbp)
movq $14,-17(%rbp)
movq $25,-9(%rbp)
movb $1,-1(%rbp)
movq -41(%rbp),%rax
cmpq $10,%rax
jg .L1
movq -33(%rbp),%rax
cmpq $40,%rax
jge .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L1:
movq -41(%rbp),%rax
movq -33(%rbp),%r10
cmpq %r10,%rax
jge .L4
jmp .L2
.L4:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
movq -41(%rbp),%rax
cmpq $10,%rax
jg .L5
movq -33(%rbp),%rax
cmpq $40,%rax
jl .L5
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L6
.L5:
movq -17(%rbp),%rax
cmpq $16,%rax
jl .L7
.L8:
movq -9(%rbp),%rdx
movq -17(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L6
.L7:
movq -9(%rbp),%rax
cmpq $27,%rax
jge .L9
movq -17(%rbp),%rax
movq -9(%rbp),%r10
cmpq %r10,%rax
jl .L9
jmp .L8
.L9:
subq $32, %rsp
movq $5,-65(%rbp)
movq $7,-57(%rbp)
movq $10,-49(%rbp)
movq -49(%rbp),%rcx
movq -57(%rbp),%rdx
movq -65(%rbp),%rsi
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $32, %rsp
.L6:
leave
ret
jmp .L9
jmp .L5
jmp .L1

